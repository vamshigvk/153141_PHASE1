package com.cg.mypaymentapp.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.service.WalletService;
import com.cg.mypaymentapp.service.WalletServiceImpl;

public class TestClass {

	WalletService service;
	Map<String, Customer> data;
	Customer cust1, cust2, cust3;

	@Before
	public void initData() {
		data = new HashMap<String, Customer>();
		cust1 = new Customer("Amit", "9900112212", new Wallet(new BigDecimal(9000)));
		cust2 = new Customer("Ajay", "9963242422", new Wallet(new BigDecimal(6000)));
		cust3 = new Customer("Yogini", "9922950519", new Wallet(new BigDecimal(7000)));

		data.put("9900112212", cust1);
		data.put("9963242422", cust2);
		data.put("9922950519", cust3);
		service = new WalletServiceImpl(data);

	}

	@Test(expected = NullPointerException.class)
	public void testCreateAccount() {
		service.createAccount(null, null, null);
	}

	@Test
	public void testCreateAccount2() {
		Customer cust = new Customer();
		cust = service.createAccount("Raswini", "8019413949", new BigDecimal(7000));
		assertEquals("Raswini", cust.getName());
	}

	@Test
	public void testCreateAccount3() {
		Customer cust = new Customer();
		cust = service.createAccount("Kavya", "9900112213", new BigDecimal(7000));
		assertEquals("9900112213", cust.getMobileNo());
	}
	
	@Test(expected = InvalidInputException.class)
	public void testCreateAccount4() {
		service.createAccount("Raswini", "8019413949", new BigDecimal("092"));
	}

	@Test
	public void testShowBalance() {
		Customer customer = service.showBalance("9963242422");
		assertEquals(new BigDecimal(6000), customer.getWallet().getBalance());
	}

	@Test(expected = InvalidInputException.class)
	public void testShowBalance2() {
		Customer cust = new Customer();
		cust = service.showBalance("9579405744");
	}

	@Test
	public void testShowBalance3() {
		Customer cust = new Customer();
		cust = service.showBalance("9922950519");
		assertEquals(cust, cust3);
	}

	@Test
	public void testShowBalance4() {
		Customer cust = new Customer();
		cust = service.showBalance("9900112212");
		BigDecimal actual = cust.getWallet().getBalance();
		BigDecimal expected = new BigDecimal(9000);
		assertEquals(expected, actual);
	}

	@Test
	public void testFundTransfer() {
		Customer c = service.fundTransfer("9912905432", "8121221121", new BigDecimal(3000));
		assertEquals(new BigDecimal("30230"), c.getWallet().getBalance());
	}

	@Test(expected = InvalidInputException.class)
	public void testFundTransfer2() {
		service.fundTransfer(null, null, new BigDecimal(7000));
	}

	@Test
	public void testFundTransfer3() {
		Customer customer = service.fundTransfer("9900112212", "9963242422", new BigDecimal(2000));
		BigDecimal actual = cust1.getWallet().getBalance();
		BigDecimal expected = new BigDecimal(7000);
		assertEquals(expected, actual);
	}

	@Test(expected = InsufficientBalanceException.class)
	public void testFundTransfer4() {
		Customer c = service.fundTransfer("9900112212", "9963242422", new BigDecimal(9000));
	}

	@Test(expected = InsufficientBalanceException.class)
	public void testFundTransfer5() {
		Wallet wallet = new Wallet(new BigDecimal(300));
		cust1.setWallet(wallet);
		data.put(cust1.getMobileNo(), cust1);
		Customer c = service.fundTransfer("9900112212", "9963242422", new BigDecimal(9000));
	}

	@Test(expected = InsufficientBalanceException.class)
	public void testFundTransfer6() {
		Wallet wallet = new Wallet(new BigDecimal(300));
		cust1.setWallet(wallet);
		data.put(cust1.getMobileNo(), cust1);
		service.fundTransfer("9900112212", "9963242422", new BigDecimal(9000));
	}
	
	@Test(expected = InvalidInputException.class)
	public void testFundTransfer7() {
		service.fundTransfer("9900112212", "8019413949", new BigDecimal("092"));
	}

	@Test
	public void testDeposit() {
		Customer customer = service.depositAmount("9963242422", new BigDecimal(3000));
		assertEquals(new BigDecimal(9000), customer.getWallet().getBalance());
	}

	@Test(expected = InvalidInputException.class)
	public void testDeposit2() {
		service.depositAmount("900000000", new BigDecimal(2000));
	}

	@Test
	public void testDeposit3() {
		Customer cust1 = service.depositAmount("9963242422", new BigDecimal(2000));
		BigDecimal actual = cust1.getWallet().getBalance();
		BigDecimal expected = new BigDecimal(8000);
		assertEquals(expected, actual);
	}

	@Test
	public void testWithdraw() {
		Customer customer = service.withdrawAmount("9963242422", new BigDecimal(3000));
		assertEquals(new BigDecimal(3000), customer.getWallet().getBalance());
	}

	@Test(expected = InvalidInputException.class)
	public void testWithdraw2() {
		service.withdrawAmount("900000000", new BigDecimal(2000));
	}

	@Test
	public void testWithdraw3() {
		Customer customer = service.withdrawAmount("9963242422", new BigDecimal(2000));
		BigDecimal actual = cust1.getWallet().getBalance();
		BigDecimal expected = new BigDecimal(2000);
		assertEquals(expected, actual);
	}

	@Test(expected = InsufficientBalanceException.class)
	public void testWithdraw4() {
		service.withdrawAmount("9963242422", new BigDecimal(6000));
	}

	@Test(expected = InsufficientBalanceException.class)
	public void testWithdraw5() {
		Wallet wallet = new Wallet(new BigDecimal(300));
		cust1.setWallet(wallet);
		data.put(cust1.getMobileNo(), cust1);
		service.withdrawAmount("9900112212", new BigDecimal(9000));
	}

	@Test(expected = InvalidInputException.class)
	public void testName() {
		service.createAccount("kavya", "9900112213", new BigDecimal(7000));
	}

	@Test(expected = InvalidInputException.class)
	public void testMobileNumber() {
		service.createAccount("Kavya", "990011221", new BigDecimal(7000));
	}

	@After
	public void testAfter() {
		service = null;
	}
}