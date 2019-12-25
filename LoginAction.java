package com.internousdev.django.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.django.dao.CartInfoDAO;
import com.internousdev.django.dao.UserInfoDAO;
import com.internousdev.django.dto.CartInfoDTO;
import com.internousdev.django.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware {

	private String userId;
	private String password;
	private String isNotUserInfoMessage;
	private boolean savedUserIdFlg;
	private String result;
	private List<String>userIdCheckList = new ArrayList<String>();
	private List<String>passwordCheckList = new ArrayList<String>();
	private List<CartInfoDTO> cartInfoDTOList = new ArrayList<CartInfoDTO>();
	private int totalPrice;
	private Map<String,Object>session = new HashMap<String,Object>();

	public String execute() {
		result = ERROR;
		UserInfoDAO userInfoDAO = new UserInfoDAO();
		session.remove("savedUserIdFlg");

//		①－A：ユーザー登録画面から遷移した場合は次の処理を行う
		if (session.containsKey("createUserFlg")) {
			userId = session.get("userIdForCreateUser").toString();
			session.remove("createUserFlg");
			session.remove("userIdForCreateUser");
		} else {
//			①－B：ログイン画面から遷移した場合は次の処理を行う
			InputChecker inputChecker = new InputChecker();
			userIdCheckList = inputChecker.doCheck("ユーザーID", userId, 1, 8, true, false, false, true, false, false);
			passwordCheckList = inputChecker.doCheck("パスワード", password, 1, 16, true, false, false, true, false, false);
//			ユーザーIDとパスワードの入力チェック(入力の有無,桁数,文字種)がOKなら認証チェックに進む
			if (userIdCheckList.size() > 0 || passwordCheckList.size() > 0) {
				return result;
			}
//			認証チェックがOKなら②に進む
			if (!(userInfoDAO.isExistUser(userId, password))) {
				isNotUserInfoMessage = "ユーザーIDまたはパスワードが異なります。";
				return result;
			}
		}

//		②仮ユーザーIDのカート情報を紐づけする
		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		String kariUserId = session.get("kariUserId").toString();
		cartInfoDTOList = cartInfoDAO.getCartList(kariUserId);
//		③仮ユーザーのカート情報をログインしたユーザーのカート情報として更新する
		if (cartInfoDTOList.size() > 0) {
			boolean cartresult = changeCartInfo(cartInfoDTOList, kariUserId);
			if (!cartresult) {
				return "DBError";
			}
		}

//		④ユーザーIDとログインフラグを「session」に入れる
		session.put("userId", userId);
		session.put("loginFlg", 1);

		if (savedUserIdFlg) {
			session.put("savedUserIdFlg", true);
		}

		session.remove("kariUserId");

		if (session.containsKey("cartFlg")) {
			session.remove("cartFlg");
			cartInfoDTOList = cartInfoDAO.getCartList(userId);
			setTotalPrice(cartInfoDAO.getTotalPrice(userId));
			result = "cart";
		} else {
			result = SUCCESS;
		}
		return result;
	}

//	③から呼ばれるメソッド
	private boolean changeCartInfo(List<CartInfoDTO> cartInfoForKariUser, String kariUserId) {
		int count = 0;
		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		boolean result = false;

		for (CartInfoDTO dto : cartInfoForKariUser) {
			if (cartInfoDAO.checkProduct(userId, dto.getProductId())) {
				count += cartInfoDAO.updateProduct(userId, dto.getProductId(), dto.getProductCount());
				cartInfoDAO.deleteProduct(kariUserId, String.valueOf(dto.getProductId()));
			} else {
				count += cartInfoDAO.joinCart(userId, kariUserId, dto.getProductId());
			}
		}
		if (count == cartInfoForKariUser.size()) {
			result = true;
		}
		return result;
	}

//	ここから「getter」と「setter」----------------------------
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getIsNotUserInfoMessage() {
		return isNotUserInfoMessage;
	}
	
	public void setIsNotUserInfoMessage(String isNotUserInfoMessage) {
		this.isNotUserInfoMessage = isNotUserInfoMessage;
	}
	
	public boolean getSavedUserIdFlg() {
		return savedUserIdFlg;
	}
	
	public void setSavedUserIdFlg(boolean savedUserIdFlg) {
		this.savedUserIdFlg  =savedUserIdFlg;
	}
	
	public Map<String,Object> getSession() {
		return session;
	}
	
	public void setSession(Map<String,Object> session) {
		this.session = session;
	}
	
	public List<String> getUserIdCheckList() {
		return userIdCheckList;
	}
	
	public void setUserIdCheckList(List<String> userIdCheckList) {
		this.userIdCheckList = userIdCheckList;
	}
	
	public List<String> getPasswordCheckList() {
		return passwordCheckList;
	}
	
	public void setPasswordCheckList(List<String>passwordCheckList) {
		this.passwordCheckList = passwordCheckList;
	}
	
	public List<CartInfoDTO> getCartInfoDTOList() {
	    return cartInfoDTOList;
	}
	
	public void setCartInfoDTOList(List<CartInfoDTO> cartInfoDTOList) {
	    this.cartInfoDTOList = cartInfoDTOList;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
}