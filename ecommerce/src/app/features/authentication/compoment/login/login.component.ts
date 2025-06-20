import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-login',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  isSubmitted = false;
  isLoading = false;
  showPassword = false;
  errorMessage = '';
  fieldErrors: { [key: string]: string } = {};

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  onSubmit() {
    this.isSubmitted = true;
    this.errorMessage = '';
    this.fieldErrors = {};

    // Chỉ kiểm tra required validation
    if (this.loginForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.isLoading = true;
    const loginRequest = this.createLoginRequest();

    console.log('Sending login request:', { ...loginRequest, password: '[HIDDEN]' });

    this.authService.requestLogin(loginRequest).subscribe({
      next: (response) => {
        this.isLoading = false;
        console.log('Login request successful:', response);
        this.handleLoginSuccess(response);
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Login request failed:', error);
        this.handleLoginError(error);
      }
    });
  }

  private createLoginRequest(): any {
    const email = this.loginForm.get('Email')?.value;
    const password = this.loginForm.get('password')?.value;
    const isEmail = email.includes('@');

    return isEmail
      ? { email: email, password: password }
      : { username: email, password: password };
  }

  private handleLoginSuccess(response: any): void {
    const email = this.loginForm.get('email')?.value;

    // Validate response has sessionId
    if (!response.sessionId) {
      console.error('No sessionId received from server:', response);
      this.errorMessage = 'Lỗi server: Không nhận được session ID';
      return;
    }

    // Store session data in localStorage - chỉ lưu username/email và sessionId
    localStorage.setItem('loginUsername', email);

    console.log('Stored session data:', {
      username: email,
      sessionId: response.sessionId
    });

    // Navigate to OTP verification page
    this.router.navigate(['/verify-otp-login'], {
      state: {
        sessionId: response.sessionId,
        email: email,
        loginAttempt: true
      }
    });
  }

  private handleLoginError(error: any): void {
    console.error('Login error:', error);

    // Reset field errors
    this.fieldErrors = {};

    if (error.status === 400 && error.error) {
      // Handle validation errors from backend
      if (error.error.errors) {
        // Handle field-specific validation errors
        const backendErrors = error.error.errors;
        Object.keys(backendErrors).forEach(field => {
          this.fieldErrors[field] = backendErrors[field];
        });
      } else if (error.error.message) {
        // Handle general error message
        this.errorMessage = this.getLocalizedErrorMessage(error.error.message);
      }
    } else if (error.error && error.error.message) {
      // Handle other error messages from backend
      this.errorMessage = this.getLocalizedErrorMessage(error.error.message);
    } else if (error.message) {
      // Handle network or other errors
      this.errorMessage = this.getLocalizedErrorMessage(error.message);
    } else {
      // Default error message
      this.errorMessage = 'Đã xảy ra lỗi. Vui lòng thử lại sau.';
    }
  }

  private getLocalizedErrorMessage(message: string): string {
    const errorMap: { [key: string]: string } = {
      'User not found': 'Không tìm thấy người dùng',
      'User account is not active': 'Tài khoản chưa được kích hoạt',
      'Incorrect password': 'Mật khẩu không chính xác',
      'Email already exists': 'Email đã tồn tại',
      'Username already exists': 'Tên đăng nhập đã tồn tại',
      'Phone number already exists': 'Số điện thoại đã tồn tại'
    };

    return errorMap[message] || message || 'Tên đăng nhập hoặc mật khẩu không đúng';
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  goToRegister() {
    this.router.navigate(['/register']);
  }

  markFormGroupTouched() {
    Object.keys(this.loginForm.controls).forEach(key => {
      const control = this.loginForm.get(key);
      control?.markAsTouched();
    });
  }

  getEmailError(): string {
    const control = this.loginForm.get('email');
    if (control?.touched && control?.errors) {
      if (control.errors['required']) return 'Vui lòng nhập email';
      if (control.errors['email']) return 'Email không hợp lệ';
    }
    return '';
  }

  getPasswordError(): string {
    const control = this.loginForm.get('password');

    // Hiển thị lỗi từ backend trước
    if (this.fieldErrors['password']) {
      return this.fieldErrors['password'];
    }

    // Sau đó mới hiển thị lỗi validation client-side
    if (control?.touched && control?.errors) {
      if (control.errors['required']) {
        return 'Vui lòng nhập mật khẩu';
      }
    }
    return '';
  }
}
