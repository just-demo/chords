package edu.self.web.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.self.dao.UserDao;
import edu.self.model.User;
import edu.self.web.form.UserForm;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final String USER_REGISTER = "user/register";
	private static final String USER_LOGIN = "user/login";
	private static final String USER_REGISTER_CONFIRMATION = "user/register/confirmation";
	//private static final String LOGIN = "spring_security_login";

	@Inject
	private UserDao userDao;

	@RequestMapping("/register")
	public String register(@ModelAttribute("user") UserForm userForm) {
		return USER_REGISTER;
	}
	
	@RequestMapping("/login")
	public String login(@ModelAttribute("user") UserForm userForm) {
		return USER_LOGIN;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/register", params = "submit")
	public String registerSubmit(@Valid @ModelAttribute("user") UserForm userForm, BindingResult bindingResult, Model model) {
		if (!bindingResult.hasErrors()) {
			if (!userForm.getPasswordConfirm().equals(userForm.getPassword())) {
				bindingResult.rejectValue("passwordConfirm", "password.confirmation.not.match", "Confirmation password does not match");
			}

			if (userDao.getUserByName(userForm.getUsername()) != null) {
				bindingResult.rejectValue("username", "username.already.exists", "A user with the same name already exists");
			}
		}

		if (bindingResult.hasErrors()) {
			return USER_REGISTER;
		}

		User user = new User();
		BeanUtils.copyProperties(userForm, user);
		user.setPassword(md5(user.getPassword()));
		userDao.saveUser(user);
		model.addAttribute("loginRedirect", "/");
		return USER_REGISTER_CONFIRMATION; // "redirect:/" + LOGIN;
	}

	private String md5(String text) {
		String hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes("UTF-8"));
			hash = new BigInteger(1, md.digest()).toString(16);
			StringUtils.leftPad(hash, 32, "0");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return hash;
	}
}

/*
 * import java.security.Principal;
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.ModelMap; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestMethod;
 * 
 * @Controller public class LoginController {
 * 
 * @RequestMapping(value = "/welcome", method = RequestMethod.GET) public String
 * printWelcome(ModelMap model, Principal principal) { String name =
 * principal.getName(); model.addAttribute("username", name);
 * model.addAttribute("message", "Spring Security Custom Form example"); return
 * "hello"; }
 * 
 * @RequestMapping(value = "/login", method = RequestMethod.GET) public String
 * login(ModelMap model) { return "login"; }
 * 
 * @RequestMapping(value = "/loginfailed", method = RequestMethod.GET) public
 * String loginerror(ModelMap model) { model.addAttribute("error", "true");
 * return "login"; }
 * 
 * @RequestMapping(value = "/logout", method = RequestMethod.GET) public String
 * logout(ModelMap model) { return "login"; } }
 */