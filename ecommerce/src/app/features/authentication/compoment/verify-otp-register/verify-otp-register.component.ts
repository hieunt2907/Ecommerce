// verify-otp-register.component.ts - Fixed version

import { Component, OnInit, OnDestroy, ViewChildren, QueryList, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-verify-otp-register',
  imports: [CommonModule, FormsModule],
  templateUrl: './verify-otp-register.component.html',
  styleUrls: ['./verify-otp-register.component.css']
})
export class VerifyOtpRegisterComponent implements OnInit, OnDestroy {
  @ViewChildren('otpInput') otpInputs!: QueryList<ElementRef>;

  otpValues: string[] = ['', '', '', '', '', ''];
  sessionId: string = '';
  email: string = '';
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
      this.email = state['email'] || '';
      console.log('Data from navigation state:', { sessionId: this.sessionId, email: this.email });
    }
  }

  ngOnInit() {
    // FIX: Multiple fallback methods to get sessionId and email
    if (!this.sessionId || !this.email) {
      // Try to get from AuthService
      this.sessionId = this.sessionId || this.authService.getSessionId() || '';
      this.email = this.email || localStorage.getItem('registerEmail') || '';
      console.log('Data from fallback methods:', { sessionId: this.sessionId, email: this.email });
    }

    // FIX: Additional check - try to get from history state if available
    if (!this.sessionId && history.state) {
      this.sessionId = history.state.sessionId || '';
      this.email = history.state.email || '';
      console.log('Data from history state:', { sessionId: this.sessionId, email: this.email });
    }

    if (!this.sessionId || !this.email) {
      console.error('No session data found', { sessionId: this.sessionId, email: this.email });
      // Don't redirect immediately, show error message instead
      this.errorMessage = 'Phiên làm việc không hợp lệ. Vui lòng đăng ký lại.';
      return;
    }

    console.log('Component initialized with:', { sessionId: this.sessionId, email: this.email });
    this.startResendCooldown();
  }

  ngOnDestroy() {
    if (this.resendInterval) {
      clearInterval(this.resendInterval);
    }
  }

  onOtpChange(index: number, event: any) {
    const value = event.target.value;

    if (value.length > 1) {
      event.target.value = value.slice(0, 1);
      this.otpValues[index] = value.slice(0, 1);
    } else {
      this.otpValues[index] = value;
    }

    if (value && index < 5) {
      const nextInput = this.otpInputs.toArray()[index + 1];
      if (nextInput) {
        nextInput.nativeElement.focus();
      }
    }

    if (value && this.errorMessage) {
      this.errorMessage = '';
      this.clearErrorState();
    }

    if (this.isOtpComplete()) {
      this.onSubmit();
    }
  }

  onOtpKeyDown(index: number, event: KeyboardEvent) {
    if (event.key === 'Backspace' && !this.otpValues[index] && index > 0) {
      const prevInput = this.otpInputs.toArray()[index - 1];
      if (prevInput) {
        prevInput.nativeElement.focus();
      }
    }

    if (event.key === 'Enter' && this.isOtpComplete()) {
      this.onSubmit();
    }
  }

  onOtpPaste(event: ClipboardEvent) {
    event.preventDefault();
    const pasteData = event.clipboardData?.getData('text');

    if (pasteData && /^\d{6}$/.test(pasteData)) {
      for (let i = 0; i < 6; i++) {
        this.otpValues[i] = pasteData[i];
        const input = this.otpInputs.toArray()[i];
        if (input) {
          input.nativeElement.value = pasteData[i];
        }
      }

      const lastInput = this.otpInputs.toArray()[5];
      if (lastInput) {
        lastInput.nativeElement.focus();
      }

      this.errorMessage = '';
      this.clearErrorState();

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

    if (this.isLoading) {
      return;
    }

    // FIX: Check sessionId before submitting
    if (!this.sessionId) {
      this.errorMessage = 'Phiên làm việc không hợp lệ. Vui lòng đăng ký lại.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const otp = this.otpValues.join('');
    const request = {
      sessionId: this.sessionId,
      otp: otp
    };

    console.log('Submitting OTP verification:', request);

    this.authService.confirmRegister(request).subscribe({
      next: (response) => {
        console.log('OTP verification successful:', response);
        this.isLoading = false;
        this.handleRegisterSuccess(response);
      },
      error: (error) => {
        console.error('OTP verification failed:', error);
        this.isLoading = false;
        this.handleRegisterError(error);
      }
    });
  }

  private handleRegisterSuccess(response: any): void {
    localStorage.removeItem('registerEmail');
    this.authService.clearSession();

    this.router.navigate(['/login'], {
      state: { registrationSuccess: true },
      replaceUrl: true
    });
  }

  private handleRegisterError(error: any): void {
    console.error('OTP verification failed:', error);

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
      'Invalid session': 'Phiên làm việc không hợp lệ',
      'User not found': 'Người dùng không tồn tại'
    };

    return errorMap[message] || message || 'Mã OTP không chính xác';
  }

  onResendOtp() {
    if (!this.canResend || this.isResending) {
      return;
    }

    // FIX: Check sessionId before resending
    if (!this.sessionId) {
      this.errorMessage = 'Phiên làm việc không hợp lệ. Vui lòng đăng ký lại.';
      return;
    }

    this.isResending = true;
    this.errorMessage = '';

    console.log('Resending OTP for sessionId:', this.sessionId);

    this.authService.resendOtp(this.sessionId).subscribe({
      next: (response) => {
        console.log('OTP resent successfully:', response);
        this.isResending = false;

        // FIX: Update sessionId if new one is provided
        if (response.sessionId && response.sessionId !== this.sessionId) {
          console.log('Updating sessionId from', this.sessionId, 'to', response.sessionId);
          this.sessionId = response.sessionId;
          this.authService.setSessionId(response.sessionId);
        }

        this.startResendCooldown();
        this.clearOtp();

        // Show success message briefly
        const originalErrorMessage = this.errorMessage;
        this.errorMessage = 'Mã OTP đã được gửi lại thành công!';
        setTimeout(() => {
          if (this.errorMessage === 'Mã OTP đã được gửi lại thành công!') {
            this.errorMessage = '';
          }
        }, 3000);

        setTimeout(() => {
          const firstInput = this.otpInputs.toArray()[0];
          if (firstInput) {
            firstInput.nativeElement.focus();
          }
        }, 100);
      },
      error: (error) => {
        console.error('Resend OTP failed:', error);
        this.isResending = false;
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
    this.resendCooldown = 60;

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
    if (this.otpInputs) {
      this.otpInputs.forEach(input => {
        input.nativeElement.value = '';
      });
    }
    this.clearErrorState();
  }

  private addErrorState() {
    if (this.otpInputs) {
      this.otpInputs.forEach(input => {
        input.nativeElement.classList.add('error');
      });
    }
  }

  private clearErrorState() {
    if (this.otpInputs) {
      this.otpInputs.forEach(input => {
        input.nativeElement.classList.remove('error');
      });
    }
  }

  goBack() {
    localStorage.removeItem('registerEmail');
    this.authService.clearSession();
    this.router.navigate(['/register']);
  }
}
