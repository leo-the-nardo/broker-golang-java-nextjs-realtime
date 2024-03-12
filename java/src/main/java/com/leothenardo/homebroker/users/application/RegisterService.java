package com.leothenardo.homebroker.users.application;

import com.leothenardo.homebroker._common.exceptions.AlreadyExistsException;
import com.leothenardo.homebroker.users.application.dtos.SignUpProviderInput;
import com.leothenardo.homebroker.users.dtos.SignupRequest;
import com.leothenardo.homebroker.users.dtos.UserDTO;
import com.leothenardo.homebroker.users.entities.User;
import com.leothenardo.homebroker.users.repositories.UserRepository;
import com.leothenardo.homebroker.wallets.repositories.WalletRepository;
import com.leothenardo.homebroker.wallets.entities.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RegisterService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private WalletRepository walletRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private VerificationTokenService verificationTokenService;

	@Transactional
	public UserDTO signUpCredentials(SignupRequest input) {
		var email = input.getUsername();
		userService.getByUsername(email).ifPresent(user -> {
			throw new AlreadyExistsException(email);
		});
		Wallet wallet = Wallet.create();
		User user = User.createCredential(email, passwordEncoder.encode(input.getPassword()), input.getName(), wallet.getId());
		userRepository.save(user);
		walletRepository.save(wallet);
		var userDTO = UserDTO.from(user);
		verificationTokenService.send(userDTO.getUsername());
		return userDTO;
	}

	public UserDTO signUpProvider(SignUpProviderInput input) {
		Wallet wallet = Wallet.create();

		User user = userRepository.save(User.createProvider(
						input.getUsername(),
						input.getName(),
						input.getPicture(),
						input.getProvider(),
						input.getProviderId(),
						wallet.getId()
		));
		walletRepository.save(wallet);
		return UserDTO.from(user);
	}

	public boolean confirmToken(String token) {
		boolean ok = verificationTokenService.confirm(token);
		return ok;
	}
}
