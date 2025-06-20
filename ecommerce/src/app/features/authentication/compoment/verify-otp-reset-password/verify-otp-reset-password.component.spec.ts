import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerifyOtpResetPasswordComponent } from './verify-otp-reset-password.component';

describe('VerifyOtpResetPasswordComponent', () => {
  let component: VerifyOtpResetPasswordComponent;
  let fixture: ComponentFixture<VerifyOtpResetPasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerifyOtpResetPasswordComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerifyOtpResetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
