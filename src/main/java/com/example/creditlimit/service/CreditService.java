package com.example.creditlimit.service;

import com.example.creditlimit.dto.*;
import java.util.List;

public interface CreditService {
	
	AccountResponse createParent(CreateParentRequest request);

	AccountResponse createChild(CreateChildRequest request);

	CreditResponse getCredit(Long userId);

	List<AccountResponse> getChildren(Long parentId);

	PolicyResponse bookPolicy(BookPolicyRequest request);

	PaymentResponse payPolicy(PaymentRequest request);
	
	List<AccountResponse> getAllAccounts();
}
