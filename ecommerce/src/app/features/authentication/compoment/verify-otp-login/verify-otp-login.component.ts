import { Component, OnInit, ViewChildren, QueryList, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-verify-otp-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './verify-otp-login.component.html',
  styleUrls: ['./verify-otp-login.component.css']
})
export class VerifyOtpLoginComponent implements OnInit {
  @ViewChildren('otpInput') otpInputs!: QueryList<ElementRef>;

  otpValues: string[] = ['', '', '', '', '', ''];
  sessionId: string = '';
  usernameOrEmail: string = '';
  isLoading: boolean = false;
  errorMessage: string = '';
  canResend: boolean = true;
  resendCooldown: number = 0;
  resendInterval: any;
  isResending: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    // Get data from navigation state
    const navigation = this.router.getCurrentNavigation();
    const state = navigation?.extras?.state;

    if (state) {
      this.sessionId = state['sessionId'] || '';
      this.usernameOrEmail = state['usernameOrEmail'] || '';
    }
  }

  ngOnInit() {
    // Fallback to get data from localStorage if navigation state is not available
    if (!this.sessionId || !this.usernameOrEmail) {
      this.sessionId = this.authService.getSessionId() || '';
      this.usernameOrEmail = localStorage.getItem('loginUsername') || '';
    }

    // Redirect if no session data
    if (!this.sessionId || !this.usernameOrEmail) {
      console.error('No session data found');
      this.router.navigate(['/login']);
      return;
    }

    console.log('VerifyOtpLogin initialized with:', {
      sessionId: this.sessionId,
      usernameOrEmail: this.usernameOrEmail
    });

    // Start resend cooldown
    this.startResendCooldown();
  }

  ngOnDestroy() {
    if (this.resendInterval) {
      clearInterval(this.resendInterval);
    }
  }

  onOtpChange(index: number, event: any) {
    const value = event.target.value;

    // Only allow single digit
    if (value.length > 1) {
      event.target.value = value.slice(0, 1);
      this.otpValues[index] = value.slice(0, 1);
    } else {
      this.otpValues[index] = value;
    }

    // Move to next input if value is entered
    if (value && index < 5) {
      const nextInput = this.otpInputs.toArray()[index + 1];
      if (nextInput) {
        nextInput.nativeElement.focus();
      }
    }

    // Clear error when user starts typing
    if (value && this.errorMessage) {
      this.errorMessage = '';
      this.clearErrorState();
    }

    // Auto submit when OTP is complete
    if (this.isOtpComplete()) {
      this.onSubmit();
    }
  }

  onOtpKeyDown(index: number, event: KeyboardEvent) {
    // Move to previous input on backspace if current input is empty
    if (event.key === 'Backspace' && !this.otpValues[index] && index > 0) {
      const prevInput = this.otpInputs.toArray()[index - 1];
      if (prevInput) {
        prevInput.nativeElement.focus();
      }
    }

    // Submit on Enter if all fields are filled
    if (event.key === 'Enter' && this.isOtpComplete()) {
      this.onSubmit();
    }
  }

  onOtpPaste(event: ClipboardEvent) {
    event.preventDefault();
    const pasteData = event.clipboardData?.getData('text');

    if (pasteData && /^\d{6}$/.test(pasteData)) {
      // Valid 6-digit OTP
      for (let i = 0; i < 6; i++) {
        this.otpValues[i] = pasteData[i];
        const input = this.otpInputs.toArray()[i];
        if (input) {
          input.nativeElement.value = pasteData[i];
        }
      }

      // Focus last input
      const lastInput = this.otpInputs.toArray()[5];
      if (lastInput) {
        lastInput.nativeElement.focus();
      }

      // Clear any existing errors
      this.errorMessage = '';
      this.clearErrorState();

      // Auto submit after paste
      setTimeout(() => {
        if (this.isOtpComplete()) {
          this.onSubmit();
        }
      }, 100);
    }
  }

  isOtpComplete(): boolean {
    return this.otpValues.every(value => value !== '');
  }

  onSubmit() {
    if (!this.isOtpComplete()) {
      this.errorMessage = 'Vui lòng nhập đầy đủ mã OTP';
      this.addErrorState();
      return;
    }

    // Prevent multiple submissions
    if (this.isLoading) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const otp = this.otpValues.join('');
    const request = {
      sessionId: this.sessionId,
      otp: otp
    };

    console.log('Submitting OTP verification:', { ...request, otp: '[HIDDEN]' });

    this.authService.confirmLogin(request).subscribe({
      next: (response) => {
        this.isLoading = false;
        console.log('OTP verification successful:', response);
        this.handleLoginSuccess(response);
      },
      error: (error) => {
        this.isLoading = false;
        console.error('OTP verification failed:', error);
        this.handleLoginError(error);
      }
    });
  }

  private handleLoginSuccess(response: any): void {
    // Store JWT token
    if (response.token) {
      localStorage.setItem('authToken', response.token);
      localStorage.setItem('isLoggedIn', 'true');
    }

    // Clean up temporary session data
    localStorage.removeItem('loginUsername');
    this.authService.clearSession();

    // Navigate to dashboard or home page
    this.router.navigate(['/'], { replaceUrl: true });
  }

  private handleLoginError(error: any): void {
    console.error('Login verification error:', error);

    if (error.status === 400 && error.error) {
      if (error.error.message) {
        this.errorMessage = this.getLocalizedErrorMessage(error.error.message);
      } else {
        this.errorMessage = 'Mã OTP không chính xác';
      }
    } else if (error.error && error.error.message) {
      this.errorMessage = this.getLocalizedErrorMessage(error.error.message);
    } else {
      this.errorMessage = 'Đã xảy ra lỗi. Vui lòng thử lại.';
    }

    this.addErrorState();
    this.clearOtp();
  }

  private getLocalizedErrorMessage(message: string): string {
    const errorMap: { [key: string]: string } = {
      'Invalid OTP': 'Mã OTP không chính xác',
      'OTP expired': 'Mã OTP đã hết hạn',
      'Session not found': 'Phiên làm việc không tồn tại',
      'Session expired': 'Phiên làm việc đã hết hạn',
      'Invalid session': 'Phiên làm việc không hợp lệ'
    };

    return errorMap[message] || message || 'Mã OTP không chính xác';
  }

  onResendOtp() {
    if (!this.canResend || this.isResending) {
      return;
    }

    this.isResending = true;
    this.errorMessage = '';

    console.log('Resending OTP for sessionId:', this.sessionId);

    this.authService.resendOtp(this.sessionId).subscribe({
      next: (response) => {
        this.isResending = false;
        console.log('OTP resend successful:', response);

        // Update sessionId if new one is provided
        if (response.sessionId) {
          this.sessionId = response.sessionId;
        }

        // Show success message briefly
        this.errorMessage = '';

        // Reset cooldown
        this.startResendCooldown();

        // Clear current OTP values
        this.clearOtp();

        // Focus first input
        setTimeout(() => {
          const firstInput = this.otpInputs.toArray()[0];
          if (firstInput) {
            firstInput.nativeElement.focus();
          }
        }, 100);
      },
      error: (error) => {
        this.isResending = false;
        console.error('OTP resend failed:', error);

        if (error.status === 400 && error.error && error.error.message) {
          this.errorMessage = this.getLocalizedErrorMessage(error.error.message);
        } else {
          this.errorMessage = 'Không thể gửi lại mã OTP. Vui lòng thử lại.';
        }
      }
    });
  }

  private startResendCooldown() {
    this.canResend = false;
    this.resendCooldown = 60; // 60 seconds cooldown

    this.resendInterval = setInterval(() => {
      this.resendCooldown--;
      if (this.resendCooldown <= 0) {
        this.canResend = true;
        clearInterval(this.resendInterval);
      }
    }, 1000);
  }

  private clearOtp() {
    this.otpValues = ['', '', '', '', '', ''];
    this.otpInputs.forEach(input => {
      input.nativeElement.value = '';
    });
    this.clearErrorState();
  }

  private addErrorState() {
    this.otpInputs.forEach(input => {
      input.nativeElement.classList.add('error');
    });
  }

  private clearErrorState() {
    this.otpInputs.forEach(input => {
      input.nativeElement.classList.remove('error');
    });
  }

  goBack() {
    // Clean up session data
    localStorage.removeItem('loginUsername');
    this.authService.clearSession();

    this.router.navigate(['/login']);
  }
}
