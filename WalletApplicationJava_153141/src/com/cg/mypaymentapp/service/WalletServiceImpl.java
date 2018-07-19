
package com.cg.mypaymentapp.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.repo.WalletRepo;
import com.cg.mypaymentapp.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {
	static Logger myLogger = Logger.getLogger(WalletServiceImpl.class);
	private WalletRepo repo = new WalletRepoImpl();

	public WalletServiceImpl(WalletRepo repo) {
		super();
		myLogger.info("constructor called");
		this.repo = repo;
	}

	public WalletServiceImpl() {

	}

	public WalletServiceImpl(Map<String, Customer> data) {
		// TODO Auto-generated constructor stub
	}

	public Customer createAccount(String name, String mobileNo, BigDecimal amount) {
		if (!isValidName(name) || !isValidMobile(mobileNo) || !isValidAmount(amount)) {
			throw new InvalidInputException("Sorry , your details are incorrect");
		}
		Customer customer = new Customer(name, mobileNo, new Wallet(amount));
		myLogger.info("create account");
		boolean b = repo.save(customer);
		return customer;
	}

	public Customer showBalance(String mobileNo) {
		if (!isValidMobile(mobileNo)) {
			throw new InvalidInputException("Invalid Mobile number");
		} else {
			Customer customer = repo.findOne(mobileNo);
			myLogger.info("show balance");
			if (customer != null)
				return customer;
			else
				throw new InvalidInputException("account with mobile number not found ");
		}
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) {
		if (!isValidMobile(sourceMobileNo) || !isValidMobile(targetMobileNo) || !isValidAmount(amount)) {
			throw new InvalidInputException("Sorry , your details are incorrect");
		}
		Customer sourceCustomer = repo.findOne(sourceMobileNo);
		Customer destinationCustomer = repo.findOne(targetMobileNo);

		if (sourceCustomer != null && destinationCustomer != null) {
			Wallet sourceBalance = sourceCustomer.getWallet();
			Wallet destinationBalance = destinationCustomer.getWallet();
			if (sourceBalance.getBalance().compareTo(amount) > 0) {
				BigDecimal remainingBalance = sourceBalance.getBalance().subtract(amount);
				BigDecimal addedBalance = destinationBalance.getBalance().add(amount);
				sourceBalance.setBalance(remainingBalance);
				destinationBalance.setBalance(addedBalance);
				myLogger.info("fund transfer");
				return sourceCustomer;
			} else {
				throw new InsufficientBalanceException("insufficient balance");
			}
		} else {
			throw new InvalidInputException("account with mobile number not found ");
		}

	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		if (!isValidMobile(mobileNo) || !isValidAmount(amount)) {
			throw new InvalidInputException("Sorry , your details are incorrect");
		}
		Customer customer = repo.findOne(mobileNo);
		if (customer != null) {
			myLogger.info("deposit money");
			Wallet balance = customer.getWallet();
			balance.setBalance(balance.getBalance().add(amount));
			return customer;
		} else {
			throw new InvalidInputException("account with mobile number not found ");
		}
	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		if (!isValidMobile(mobileNo) || !isValidAmount(amount)) {
			throw new InvalidInputException("Sorry , your details are incorrect");
		}
		Customer customer = repo.findOne(mobileNo);
		if (customer != null) {
			Wallet balance = customer.getWallet();
			if (balance.getBalance().compareTo(amount) > 0) {
				BigDecimal addedBalance = balance.getBalance().subtract(amount);
				balance.setBalance(addedBalance);
				myLogger.info("withdraw money");
				return customer;
			} else {
				throw new InsufficientBalanceException("Insufficient balance ");
			}
		} else {
			throw new InvalidInputException("account with mobile number not found ");
		}
	}

	private boolean isValidMobile(String mobileNo) {
		if (String.valueOf(mobileNo).matches("[1-9][0-9]{9}")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isValidAmount(BigDecimal amount) {
		BigDecimal val = new BigDecimal("0");
		if (amount.compareTo(val) > 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isValidName(String name) {
		if (name.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
}
