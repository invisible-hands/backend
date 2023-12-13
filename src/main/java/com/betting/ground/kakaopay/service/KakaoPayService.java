package com.betting.ground.kakaopay.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.kakaopay.dto.request.KakaoReadyRequest;
import com.betting.ground.kakaopay.dto.response.KakaoApproveResponse;
import com.betting.ground.kakaopay.dto.response.KakaoReadyResponse;
import com.betting.ground.user.domain.Payment;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.repository.PaymentRepository;
import com.betting.ground.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {

	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;

	@Value("${kakao.admin.key}")
	private String adminKey;
	private static final String cid = "TC0ONETIME";
	private KakaoReadyResponse kakaoReady;

	public KakaoReadyResponse kakaoPayReady(KakaoReadyRequest request, Long userId) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", cid);
		parameters.add("partner_order_id", "partner_order_id");
		parameters.add("partner_user_id", "partner_user_id");
		parameters.add("item_name", "BettingGround 가상머니 충전");
		parameters.add("quantity", "1");
		parameters.add("total_amount", request.getPrice().toString());
		parameters.add("vat_amount", "0");
		parameters.add("tax_free_amount", "0");
		parameters.add("approval_url", "http://localhost:5173/pay/success"); // 성공 시 redirect url
		parameters.add("cancel_url", "http://localhost:5173/pay/cancel"); // 취소 시 redirect url
		parameters.add("fail_url", "http://localhost:5173/pay/fail"); // 실패 시 redirect url

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

		// 프록시 설정 추가
		RestTemplate restTemplate = new RestTemplate();
		// SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		// factory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("krmp-proxy.9rum.cc", 3128)));
		// restTemplate.setRequestFactory(factory);

		kakaoReady = restTemplate.postForObject(
			"https://kapi.kakao.com/v1/payment/ready",
			requestEntity,
			KakaoReadyResponse.class);

		kakaoReady.setUserId(userId);

		return kakaoReady;
	}

	public KakaoApproveResponse approveResponse(String pgToken) {

		// 카카오 요청
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", cid);
		parameters.add("tid", kakaoReady.getTid());
		parameters.add("partner_order_id", "partner_order_id");
		parameters.add("partner_user_id", "partner_user_id");
		parameters.add("pg_token", pgToken);

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

		RestTemplate restTemplate = new RestTemplate();
		// SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		// factory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("krmp-proxy.9rum.cc", 3128)));
		// restTemplate.setRequestFactory(factory);
		KakaoApproveResponse approveResponse = restTemplate.postForObject(
			"https://kapi.kakao.com/v1/payment/approve",
			requestEntity,
			KakaoApproveResponse.class);

		User user = userRepository.findById(kakaoReady.getUserId()).orElseThrow(
			() -> new GlobalException(ErrorCode.BAD_REQUEST)
		);
		user.increaseMoney(approveResponse.getAmount().getTotal());

		Payment payment = new Payment(approveResponse, user);
		paymentRepository.save(payment);

		return approveResponse;
	}

	private HttpHeaders getHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();

		String auth = "KakaoAK " + adminKey;

		httpHeaders.set("Authorization", auth);
		httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		return httpHeaders;
	}
}
