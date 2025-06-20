import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    RouterLink,
    CommonModule,
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  forgotPasswordForm: FormGroup;
  isLoading = false;
  isSubmitted = false;
  email = '';
  errorMessage = '';
  successMessage = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    this.forgotPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit() {
    this.isSubmitted = true;
    this.errorMessage = '';
    this.successMessage = false;

    if (this.forgotPasswordForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.isLoading = true;
    this.email = this.forgotPasswordForm.get('email')?.value.trim();

    this.authService.forgotPassword(this.email).subscribe({
      next: (response) => {
        console.log('Forgot password response:', response);
        this.isLoading = false;
        this.successMessage = true;

        // Store email and sessionId for OTP verification
        localStorage.setItem('resetEmail', this.email);

        this.router.navigate(['/verify-otp-reset-password'], {
          state: {
            email: this.email,
            sessionId: response.sessionId
          },
          replaceUrl: true
        });
      },
      error: (error) => {
        console.error('Forgot password error:', error);
        this.isLoading = false;
        this.errorMessage = this.getErrorMessage(error);
      }
    });
  }

  private getErrorMessage(error: any): string {
    if (error.error?.message) {
      const message = error.error.message;
      if (message.includes('User not found')) return 'Email không tồn tại';
      return message;
    }
    return 'Đã xảy ra lỗi khi gửi yêu cầu. Vui lòng thử lại.';
  }

  markFormGroupTouched() {
    Object.keys(this.forgotPasswordForm.controls).forEach(key => {
      const control = this.forgotPasswordForm.get(key);
      control?.markAsTouched();
    });
  }

  getEmailError(): string {
    const control = this.forgotPasswordForm.get('email');
    if (control?.touched && control?.errors) {
      if (control.errors['required']) return 'Vui lòng nhập email';
      if (control.errors['email']) return 'Email không hợp lệ';
    }
    return '';
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
