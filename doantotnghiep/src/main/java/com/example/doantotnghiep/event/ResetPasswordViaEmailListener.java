package com.example.doantotnghiep.event;


import com.example.doantotnghiep.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class ResetPasswordViaEmailListener implements ApplicationListener<OnResetPasswordViaEmailEvent> {

	@Autowired
	private IEmailService emailService;

	@Override
	public void onApplicationEvent(final OnResetPasswordViaEmailEvent event) {
		sendResetPasswordViaEmail(event.getEmail());
	}

	private void sendResetPasswordViaEmail(String email) {
		emailService.sendResetPassword(email);
	}

}