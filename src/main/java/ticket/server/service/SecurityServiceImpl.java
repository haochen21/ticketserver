package ticket.server.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ticket.server.model.security.Customer;
import ticket.server.model.security.CustomerLogin;
import ticket.server.model.security.Device;
import ticket.server.model.security.LoginResult;
import ticket.server.model.security.Merchant;
import ticket.server.model.security.MerchantLogin;
import ticket.server.model.security.OpenRange;
import ticket.server.model.security.Password;
import ticket.server.repository.security.CustomerRepository;
import ticket.server.repository.security.DeviceRepository;
import ticket.server.repository.security.MerchantRepository;

@Service
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	DeviceRepository deviceRepository;

	@Override
	public Customer findCustomerByOpenId(String openId) {
		return customerRepository.findByOpenId(openId);
	}

	@Override
	public Merchant findMerchantByOpenId(String openId) {
		return merchantRepository.findByOpenId(openId);
	}

	@Override
	public Boolean existsCustomerByOpenId(String openId) {
		return customerRepository.existsByOpenId(openId);
	}

	@Override
	public Boolean existsMerchantByOpenId(String openId) {
		return merchantRepository.existsByOpenId(openId);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Merchant saveMerchant(Merchant merchant) {
		String pwd = Password.PASSWORD.MD5(merchant.getPassword());
		merchant.setPassword(pwd);
		return merchantRepository.save(merchant);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Merchant updateMerchant(Merchant merchant) {
		Merchant dbMerchant = merchantRepository.findOne(merchant.getId());

		dbMerchant.setName(merchant.getName());
		dbMerchant.setDeviceNo(merchant.getDeviceNo());
		dbMerchant.setPhone(merchant.getPhone());
		dbMerchant.setMail(merchant.getMail());
		dbMerchant.setShortName(merchant.getShortName());
		dbMerchant.setAddress(merchant.getAddress());
		dbMerchant.setDescription(merchant.getDescription());
		dbMerchant.setOpen(merchant.getOpen());
		dbMerchant.setTakeByPhone(merchant.getTakeByPhone());
		dbMerchant.setTakeByPhoneSuffix(merchant.getTakeByPhoneSuffix());
		return merchantRepository.save(dbMerchant);
	}

	@Override
	public Merchant findMerchant(Long merchantId) {
		return merchantRepository.findOne(merchantId);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateMerchantOpen(Long id, Boolean open) {
		merchantRepository.updateOpen(id, open);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateMerchantQrCode(Long id, String qrCode) {
		merchantRepository.updateQrCode(id, qrCode);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void registerMerchant(Long id, String deviceNo, String phone) {
		merchantRepository.register(id, deviceNo, phone);
	}

	@Override
	public Merchant findMerchantByDeviceNo(String deviceNo) {
		return merchantRepository.findByDeviceNo(deviceNo);
	}

	@Override
	public CustomerLogin customerLogin(String loginName, String password) {
		CustomerLogin login = new CustomerLogin();
		Customer customer = customerRepository.findByLoginName(loginName);
		if (customer == null) {
			login.setResult(LoginResult.LOGINNAMEERROR);
		} else {
			String pwd = Password.PASSWORD.MD5(password);
			if (!customer.getPassword().equals(pwd)) {
				login.setResult(LoginResult.PASSWORDERROR);
			} else {
				login.setResult(LoginResult.AUTHORIZED);
				customer.setPassword("");
				login.setCustomer(customer);
			}
		}
		return login;
	}

	@Override
	public MerchantLogin merchantLogin(String loginName, String password) {
		MerchantLogin login = new MerchantLogin();
		Merchant merchant = merchantRepository.findByLoginName(loginName);
		if (merchant == null) {
			login.setResult(LoginResult.LOGINNAMEERROR);
		} else {
			String pwd = Password.PASSWORD.MD5(password);
			if (!merchant.getPassword().equals(pwd)) {
				login.setResult(LoginResult.PASSWORDERROR);
			} else {
				login.setResult(LoginResult.AUTHORIZED);
				merchant.setPassword("");
				login.setMerchant(merchant);
			}
		}
		return login;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Customer saveCustomer(Customer customer) {
		String pwd = Password.PASSWORD.MD5(customer.getPassword());
		customer.setPassword(pwd);
		return customerRepository.save(customer);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Customer updateCustomer(Customer customer) {
		Customer dbCustomer = customerRepository.findOne(customer.getId());

		dbCustomer.setName(customer.getName());
		dbCustomer.setCardNo(customer.getCardNo());
		dbCustomer.setPhone(customer.getPhone());
		dbCustomer.setMail(customer.getMail());
		dbCustomer.setCardUsed(customer.getCardUsed());

		return customerRepository.save(dbCustomer);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateCustomerPhone(Long id, String phone) {
		customerRepository.updatePhone(id, phone);
	}

	@Override
	public Customer findCustomer(Long customerId) {
		return customerRepository.findOne(customerId);
	}

	@Override
	public Customer findCustomerByCardNo(String cardNo) {
		return customerRepository.findByCardNo(cardNo);
	}

	@Override
	public Customer findCustomerByPhone(String phone) {
		return customerRepository.findByPhone(phone);
	}

	@Override
	public Customer findCustomerByFullPhone(String phone) {
		return customerRepository.findByFullPhone(phone);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void modifyCustomerPassword(Long id, String password) {
		String pwd = Password.PASSWORD.MD5(password);
		Customer customer = customerRepository.findOne(id);
		customer.setPassword(pwd);
		customerRepository.save(customer);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void modifyMerchantPassword(Long id, String password) {
		String pwd = Password.PASSWORD.MD5(password);
		Merchant merchant = merchantRepository.findOne(id);
		merchant.setPassword(pwd);
		merchantRepository.save(merchant);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Merchant updateOpenRange(Long merchantId, Collection<OpenRange> ranges) {
		Merchant merchant = merchantRepository.findOne(merchantId);
		merchant.setOpenRanges(ranges);
		merchant = merchantRepository.save(merchant);
		Merchant dbMerchant = merchantRepository.findWithOpenRange(merchantId);
		return dbMerchant;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateMerchantImageSource(Long id, String imageSource) {
		merchantRepository.updateImageSource(id, imageSource);
	}

	@Override
	public Merchant findMerchantWithOpenRange(Long id) {
		return merchantRepository.findWithOpenRange(id);
	}

	@Override
	public Boolean existsCustomerByLoginName(String loginName) {
		return customerRepository.existsByLoginName(loginName);
	}

	@Override
	public Boolean existsMerchantByLoginName(String loginName) {
		return merchantRepository.existsByLoginName(loginName);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Device createDevice(Device device) {
		return deviceRepository.save(device);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteDevice(Device device) {
		deviceRepository.delete(device);
	}

	@Override
	public Device findByPhone(String phone) {
		return deviceRepository.findByPhone(phone);
	}

	@Override
	public Device findByNo(String no) {
		return deviceRepository.findByNo(no);
	}

	@Override
	public Boolean existsDeviceByNo(String no) {
		return deviceRepository.existsByNo(no);
	}

	@Override
	public Boolean existsDeviceByPhone(String phone) {
		return deviceRepository.existsByPhone(phone);
	}

	@Override
	public Boolean existsByCardNo(String cardNo) {
		return customerRepository.existsByCardNo(cardNo);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Set<Merchant> saveMerchantsOfCustomer(Long customerId, Set<Long> merchantIds) {
		Customer customer = customerRepository.findOne(customerId);
		customer.getMerchants().clear();
		for (Long merchantId : merchantIds) {
			Merchant m = merchantRepository.findOne(merchantId);
			customer.getMerchants().add(m);
		}
		customerRepository.save(customer);
		return customer.getMerchants();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Set<Merchant> findMerchantsOfCustomer(Long customerId) {
		Customer customer = customerRepository.findOne(customerId);
		customer.getMerchants().size();
		return customer.getMerchants();
	}

	@Override
	public List<Merchant> findMerchantsByName(String name) {
		return merchantRepository.findByName(name);
	}

	@Override
	public Boolean existsCustomerByPhone(String phone) {
		return customerRepository.existsByPhone(phone);
	}

	@Override
	public Boolean existsMerchantByPhone(String phone) {
		return merchantRepository.existsByPhone(phone);
	}
}
