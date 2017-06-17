package com.timxyz.controllers;

import javax.validation.Valid;

import com.timxyz.controllers.forms.MyAccount.MyAccountUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.timxyz.models.Account;
import com.timxyz.services.AccountService;
import com.timxyz.services.TokenAuthenticationService;
import com.timxyz.services.exceptions.ServiceException;

@RestController
public class MyAccountController extends BaseController<Account, AccountService> {

	@ResponseBody
	@GetMapping("/myAccount")
	public ResponseEntity get(@RequestHeader("Authorization") String token) throws ServiceException {
		Account account = TokenAuthenticationService.findAccountByToken(token);
		
		if (account == null) {
			throw new ServiceException("Korisnički račun nije pronađen.");
		}

		return ResponseEntity.ok(account);
	}

	@Transactional
	@ResponseBody
	@PostMapping("/myAccount")
	public ResponseEntity update(@RequestHeader("Authorization") String token, @RequestBody @Valid MyAccountUpdateForm updatedAccount) throws ServiceException {
		Account account = TokenAuthenticationService.findAccountByToken(token);
		
		if (account == null || !BCrypt.checkpw(updatedAccount.getCurrentPassword(), account.getPassword())) {
		     throw new ServiceException("Neispravna trenutna lozinka.");
		}

		if (updatedAccount.isPasswordUpdated()) {
			account.setRawPassword(updatedAccount.getNewPassword());
		}

		account = service.save(account);

		logForUpdate(token, account);

		return ResponseEntity.ok(account);
	}
}
