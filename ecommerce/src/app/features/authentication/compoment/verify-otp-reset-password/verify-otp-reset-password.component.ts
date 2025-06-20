// verify-otp-reset-password.component.ts
import { Component, OnInit, OnDestroy, ViewChildren, QueryList, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-verify-otp-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './verify-otp-reset-password.component.html',
  styleUrls: ['./verify-otp-reset-password.component.css']
})
export class VerifyOtpResetPasswordComponent implements OnInit, OnDestroy {
  @ViewChildren('otpInput') otpInputs!: QueryList<ElementRef>;

  otpValues: string[] = ['', '', '', '', '', ''];
  email: string = '';
  sessionId: string = '';
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
    // Get email and sessionId from navigation state
    const navigation = this.router.getCurrentNavigation();
    const state = navigation?.extras?.state;
    if (state) {
      this.email = state['email'] || '';
      this.sessionId = state['sessionId'] || '';
    }
  }

  ngOnInit() {
    // Fallback to localStorage or AuthService
    if (!this.email || !this.sessionId) {
      this.email = localStorage.getItem('resetEmail') || '';
      this.sessionId = this.authService.getSessionId() || '';
    }

    if (!this.email || !this.sessionId) {
      this.errorMessage = 'Phiên làm việc không hợp lệ. Vui lòng thử lại.';
      console.error('No session data found', { sessionId: this.sessionId, email: this.email });
      return;
    }

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

    if (!this.sessionId) {
      this.errorMessage = 'Phiên làm việc không hợp lệ. Vui lòng thử lại.';
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

    this.authService.verifyResetPassword(request).subscribe({
      next: (response) => {
        console.log('OTP verification successful:', response);
        this.isLoading = false;
        localStorage.removeItem('resetEmail');
        this.router.navigate(['/reset-password'], {
          state: { email: this.email, sessionId: this.sessionId },
          replaceUrl: true
        });
      },
      error: (error) => {
        console.error('OTP verification failed:', error);
        this.isLoading = false;
        this.errorMessage = this.getErrorMessage(error);
        this.clearOtp();
        this.addErrorState();
      }
    });
  }

  private getErrorMessage(error: any): string {
    if (error.error?.message) {
      const message = error.error.message;
      const errorMap: { [key: string]: string } = {
        'Invalid OTP': 'Mã OTP không chính xác',
        'Session not found': 'Phiên làm việc không tồn tại',
        'Session expired': 'Phiên làm việc đã hết hạn',
        'User not found': 'Người dùng không tồn tại'
      };
      return errorMap[message] || message || 'Mã OTP không chính xác';
    }
    return 'Đã xảy ra lỗi. Vui lòng thử lại.';
  }

  resendOtp() {
    if (!this.canResend || this.isResending) return;

    if (!this.sessionId) {
      this.errorMessage = 'Phiên làm việc không hợp lệ. Vui lòng thử lại.';
      return;
    }

    this.isResending = true;
    this.errorMessage = '';

    console.log('Resending OTP for sessionId:', this.sessionId);

    this.authService.resendOtp(this.sessionId).subscribe({
      next: (response) => {
        console.log('OTP resent successfully:', response);
        this.isResending = false;

        // Update sessionId if new one is provided
        if (response.sessionId) {
          this.sessionId = response.sessionId;
        }

        // Show success message briefly
        this.errorMessage = 'Mã OTP đã được gửi lại thành công!';
        setTimeout(() => {
          if (this.errorMessage === 'Mã OTP đã được gửi lại thành công!') {
            this.errorMessage = '';
          }
        }, 3000);

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
        console.error('Resend OTP failed:', error);
        this.isResending = false;
        this.errorMessage = this.getErrorMessage(error);
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
    localStorage.removeItem('resetEmail');
    this.authService.clearSession();
    this.router.navigate(['/forgot-password']);
  }
}
