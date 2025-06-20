// auth.service.ts - Updated version

import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, tap, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';

interface UserCreateRequest {
  username: string;
  email: string;
  passwordHash: string; // Must match backend DTO
  phone: string;
  avartarUrl?: string;
  dateOfBirth?: Date;
  gender?: string;
  roles?: string[];
}

interface UserLoginRequest {
  email?: string;
  username?: string;
  password: string;
}

interface OtpVerificationRequest {
  sessionId: string;
  otp: string;
}

interface AuthenticationResponse {
  sessionId: string;
  message: string;
}

interface LoginResponse {
  token: string;
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = environment.apiUrl + '/auth';
  private currentSessionId: string | null = null;

  constructor(private http: HttpClient) {}

  setSessionId(sessionId: string): void {
    this.currentSessionId = sessionId;
    localStorage.setItem('currentSessionId', sessionId);
  }

  getSessionId(): string | null {
    return this.currentSessionId || localStorage.getItem('currentSessionId');
  }

  clearSession(): void {
    this.currentSessionId = null;
    localStorage.removeItem('currentSessionId');
  }

  requestLogin(request: UserLoginRequest): Observable<AuthenticationResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<AuthenticationResponse>(`${this.API_URL}/login/request`, request, { headers })
      .pipe(
        tap(response => {
          console.log('Login request response:', response);
          if (response.sessionId) {
            this.setSessionId(response.sessionId);
          }
        }),
        catchError(this.handleError)
      );
  }

  requestRegister(request: UserCreateRequest): Observable<AuthenticationResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    // FIX: Ensure the field name is 'passwordHash' to match backend DTO
    const validatedRequest = {
      username: request.username?.trim() || '',
      email: request.email?.trim() || '',
      passwordHash: request.passwordHash || '', // Use 'passwordHash' instead of 'password'
      phone: request.phone?.trim() || ''
    };

    console.log('Sending register request:', {
      ...validatedRequest,
      passwordHash: '[HIDDEN]' // Don't log actual password
    });

    return this.http.post<AuthenticationResponse>(`${this.API_URL}/register/request`, validatedRequest, { headers })
      .pipe(
        tap(response => {
          console.log('Register request response:', response);
          if (response.sessionId) {
            this.setSessionId(response.sessionId);
            localStorage.setItem('registerEmail', validatedRequest.email);
          }
        }),
        catchError(this.handleError)
      );
  }

  confirmRegister(request: OtpVerificationRequest): Observable<AuthenticationResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    console.log('Confirming register with request:', request);

    return this.http.post<AuthenticationResponse>(`${this.API_URL}/register/confirm`, request, { headers })
      .pipe(
        tap(response => {
          console.log('Register confirm response:', response);
          this.clearSession();
        }),
        catchError(this.handleError)
      );
  }

  confirmLogin(request: OtpVerificationRequest): Observable<LoginResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<LoginResponse>(`${this.API_URL}/login/confirm`, request, { headers })
      .pipe(
        tap(response => {
          console.log('Login confirm response:', response);
          this.clearSession();
        }),
        catchError(this.handleError)
      );
  }

  resendOtp(sessionId: string): Observable<AuthenticationResponse> {
    if (!sessionId || sessionId.trim() === '') {
      return throwError(() => new Error('Session ID is required'));
    }

    const params = new HttpParams().set('sessionId', sessionId.trim());

    console.log('Resending OTP with sessionId:', sessionId);

    return this.http.post<AuthenticationResponse>(`${this.API_URL}/resend-otp`, null, { params })
      .pipe(
        tap(response => {
          console.log('Resend OTP response:', response);
          if (response.sessionId) {
            this.setSessionId(response.sessionId);
          }
        }),
        catchError(this.handleError)
      );
  }

  forgotPassword(email: string): Observable<AuthenticationResponse> {
    const params = new HttpParams().set('email', email);

    return this.http.post<AuthenticationResponse>(`${this.API_URL}/password/forgot`, null, { params })
      .pipe(
        tap(response => {
          if (response.sessionId) {
            this.setSessionId(response.sessionId);
          }
        }),
        catchError(this.handleError)
      );
  }

  verifyResetPassword(request: OtpVerificationRequest): Observable<AuthenticationResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<AuthenticationResponse>(`${this.API_URL}/password/verify`, request, { headers })
      .pipe(
        catchError(this.handleError)
      );
  }

  resetPassword(request: { sessionId: string; newPassword: string }): Observable<AuthenticationResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<AuthenticationResponse>(`${this.API_URL}/password/reset`, request, { headers })
      .pipe(
        tap(() => this.clearSession()),
        catchError(this.handleError)
      );
  }

  logout(): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${this.API_URL}/logout`, {})
      .pipe(
        tap(() => this.clearSession()),
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('AuthService HTTP Error:', error);
    console.error('Error details:', {
      status: error.status,
      statusText: error.statusText,
      message: error.error?.message,
      error: error.error
    });

    let errorMessage = 'Đã xảy ra lỗi không xác định';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Lỗi kết nối: ${error.error.message}`;
    } else {
      if (error.error?.message) {
        errorMessage = error.error.message;
      } else if (error.status === 0) {
        errorMessage = 'Không thể kết nối đến server';
      } else if (error.status >= 500) {
        errorMessage = 'Lỗi server';
      } else if (error.status === 400) {
        errorMessage = 'Dữ liệu không hợp lệ';
      } else {
        errorMessage = `Lỗi server: ${error.status}`;
      }
    }

    console.error('Processed error message:', errorMessage);
    return throwError(() => error);
  }
}
