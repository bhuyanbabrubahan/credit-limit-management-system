package com.example.creditlimit.controller;

import com.example.creditlimit.dto.TransactionResponse;
import com.example.creditlimit.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
	private final TransactionService service;

	public TransactionController(TransactionService service) {
		this.service = service;
	}

	@GetMapping("/customer/{customerId}")
	public List<TransactionResponse> byCustomer(@PathVariable Long customerId) {
		return service.byCustomer(customerId);
	}

	@GetMapping("/agent/{agentId}")
	public List<TransactionResponse> byAgent(@PathVariable Long agentId) {
		return service.byAgent(agentId);
	}
}
