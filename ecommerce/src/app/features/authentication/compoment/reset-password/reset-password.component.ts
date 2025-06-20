import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  resetForm !: FormGroup;
  email: string = '';
  sessionId: string = '';
  isLoading: boolean = false;
  isSuccess: boolean = false;
  errorMessage: string = '';
  showPassword: boolean = false;
  showConfirmPassword: boolean = false;
  passwordStrength: number = 0;
  passwordStrengthText: string = '';
  passwordStrengthColor: string = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    // Get email and sessionId from navigation state
    const navigation = this.router.getCurrentNavigation();
    const state = navigation?.extras?.state;

    if (!state?.['email'] || !state?.['sessionId']) {
      this.errorMessage = 'Phiên làm việc không hợp lệ. Vui lòng thử lại.';
      this.router.navigate(['/forgot-password']);
      return;
    }

    this.email = state['email'] || '';
    this.sessionId = state['sessionId'] || '';

    this.resetForm = this.fb.group({
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        this.passwordValidator
      ]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {
    // Fallback to AuthService or localStorage for sessionId
    if (!this.sessionId) {
      this.sessionId = this.authService.getSessionId() || '';
      console.log('SessionId from fallback:', this.sessionId);
    }

    if (!this.email || !this.sessionId) {
      this.errorMessage = 'Phiên làm việc không hợp lệ. Vui lòng thử lại.';
      this.router.navigate(['/forgot-password']);
      return;
    }

    // Watch for password changes to update strength indicator
    this.resetForm.get('password')?.valueChanges.subscribe(password => {
      this.updatePasswordStrength(password);
    });
  }

  passwordValidator(control: AbstractControl) {
    const password = control.value;
    if (!password) return null;

    const hasUppercase = /[A-Z]/.test(password);
    const hasLowercase = /[a-z]/.test(password);
    const hasNumbers = /\d/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    const validConditions = [hasUppercase, hasLowercase, hasNumbers, hasSpecialChar];
    const validCount = validConditions.filter(condition => condition).length;

    if (validCount < 3) {
      return { weakPassword: true };
    }

    return null;
  }

  passwordMatchValidator(form: AbstractControl) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;

    if (password && confirmPassword && password !== confirmPassword) {
      return { passwordMismatch: true };
    }

    return null;
  }

  updatePasswordStrength(password: string) {
    if (!password) {
      this.passwordStrength = 0;
      this.passwordStrengthText = '';
      this.passwordStrengthColor = '';
      return;
    }

    const checks = [
      password.length >= 8,
      this.checkUppercase(password),
      this.checkLowercase(password),
      this.checkNumber(password),
      this.checkSpecialChar(password),
      password.length >= 12
    ];

    const strength = checks.filter(check => check).length;

    if (strength <= 2) {
      this.passwordStrength = 1;
      this.passwordStrengthText = 'Yếu';
      this.passwordStrengthColor = '#ef4444';
    } else if (strength <= 4) {
      this.passwordStrength = 2;
      this.passwordStrengthText = 'Trung bình';
      this.passwordStrengthColor = '#f59e0b';
    } else {
      this.passwordStrength = 3;
      this.passwordStrengthText = 'Mạnh';
      this.passwordStrengthColor = '#10b981';
    }
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility() {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  onSubmit() {
    if (this.resetForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    if (!this.sessionId) {
      this.errorMessage = 'Phiên làm việc không hợp lệ. Vui lòng thử lại.';
      this.router.navigate(['/forgot-password']);
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    const { password } = this.resetForm.value;

    const request = {
      sessionId: this.sessionId,
      newPassword: password
    };

    console.log('Submitting password reset:', { sessionId: this.sessionId, newPassword: '[HIDDEN]' });

    this.authService.resetPassword(request).subscribe({
      next: (response) => {
        console.log('Password reset successful:', response);
        this.isLoading = false;
        this.isSuccess = true;
        localStorage.removeItem('resetEmail');
        this.authService.clearSession();

        // Auto redirect to login after 3 seconds
        setTimeout(() => {
          this.router.navigate(['/login'], { replaceUrl: true });
        }, 3000);
      },
      error: (error) => {
        console.error('Password reset failed:', error);
        this.isLoading = false;
        this.errorMessage = this.getErrorMessage(error);
      }
    });
  }

  private getErrorMessage(error: any): string {
    if (error.error?.message) {
      const message = error.error.message;
      const errorMap: { [key: string]: string } = {
        'User not found': 'Người dùng không tồn tại',
        'Verify request reset password failed': 'Xác thực OTP không thành công',
        'Session not found': 'Phiên làm việc không tồn tại',
        'Session expired': 'Phiên làm việc đã hết hạn'
      };
      return errorMap[message] || message || 'Đã xảy ra lỗi khi đặt lại mật khẩu';
    }
    return 'Đã xảy ra lỗi. Vui lòng thử lại.';
  }

  markFormGroupTouched() {
    Object.keys(this.resetForm.controls).forEach(key => {
      const control = this.resetForm.get(key);
      control?.markAsTouched();
    });
  }

  getPasswordError(): string {
    const passwordControl = this.resetForm.get('password');
    if (passwordControl?.touched && passwordControl?.errors) {
      if (passwordControl.errors['required']) {
        return 'Vui lòng nhập mật khẩu mới';
      }
      if (passwordControl.errors['minlength']) {
        return 'Mật khẩu phải có ít nhất 8 ký tự';
      }
      if (passwordControl.errors['weakPassword']) {
        return 'Mật khẩu cần có ít nhất 3 trong 4: chữ hoa, chữ thường, số, ký tự đặc biệt';
      }
    }
    return '';
  }

  getConfirmPasswordError(): string {
    const confirmPasswordControl = this.resetForm.get('confirmPassword');
    if (confirmPasswordControl?.touched && confirmPasswordControl?.errors) {
      if (confirmPasswordControl.errors['required']) {
        return 'Vui lòng xác nhận mật khẩu';
      }
    }
    if (this.resetForm.errors?.['passwordMismatch'] && confirmPasswordControl?.touched) {
      return 'Mật khẩu xác nhận không khớp';
    }
    return '';
  }

  checkUppercase(password: string | null): boolean {
    return /[A-Z]/.test(password || '');
  }

  checkLowercase(password: string | null): boolean {
    return /[a-z]/.test(password || '');
  }

  checkNumber(password: string | null): boolean {
    return /\d/.test(password || '');
  }

  checkSpecialChar(password: string | null): boolean {
    return /[!@#$%^&*(),.?":{}|<>]/.test(password || '');
  }

  goToLogin() {
    this.authService.clearSession();
    this.router.navigate(['/login']);
  }
}
