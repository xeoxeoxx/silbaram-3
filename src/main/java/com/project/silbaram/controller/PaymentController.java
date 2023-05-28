package com.project.silbaram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.silbaram.dto.OrderInfoDTO;
import com.project.silbaram.dto.OrderListDTO;
import com.project.silbaram.service.OrderInfoServiceImpl;
import com.project.silbaram.service.OrderListServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "/payment")
public class PaymentController {

    private final OrderListServiceImpl orderListService;
    private final OrderInfoServiceImpl orderInfoService;
    private final ObjectMapper objectMapper;

    /*
    * amount: 총 가격, method: 결제 방식
    * */
    @PostMapping("/cart_connect_pay")
    public void tossPay(Model model,
                        OrderListDTO orderListDTO,
                        @RequestParam(value = "bkids[]") List<String> bkids,
                        @RequestParam(value = "method") String method,
                        HttpSession httpSession){

        String orderId = httpSession.getAttribute("orderId").toString();
        Long mid = (Long) httpSession.getAttribute("mid");
        int totalPrice = (int) httpSession.getAttribute("totalPrice");

        // cart_connect_pay.html로 전달하기 위한 용도
        model.addAttribute("bkids", bkids);
        model.addAttribute("method", method);
        model.addAttribute("orderId", orderId); // cart_connect_pay로 전달하기 위한 용도
        model.addAttribute("totalPrice", totalPrice); // cart_connect_pay로 전달하기 위한 용도

        // orderList에 추가(주문목록 출력및 주문 정보 대조용)
        orderListDTO.setMemberID(mid);
        orderListDTO.setTotalPrice(totalPrice);
        orderListDTO.setPayBy(method);
        orderListDTO.setOrderNum(orderId);
        orderListDTO.setOrderName("현재 결제기능 미완성으로 임시로 넣는 기본값입니다.");
        orderListService.registerOrderList(orderListDTO);

        // orderInfo에 추가(기존 orderList)
        for (String bkid : bkids) {
            OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
            orderInfoDTO.setBookId(Long.parseLong(bkid));
            orderInfoDTO.setOrderNum(orderId);
            orderInfoDTO.setOid(orderInfoService.showOid(orderId));
            orderInfoDTO.setMemberId(mid);
            orderInfoService.registerOrderInfo(orderInfoDTO);
        }

    }

    @GetMapping(value = "/success")
    public void paymentResult(
            Model model,
            HttpSession httpSession,
            @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "amount") Integer amount,
            @RequestParam(value = "paymentKey") String paymentKey) throws Exception {

        String secretKey = "test_ak_ZORzdMaqN3wQd5k6ygr5AkYXQGwy:";
        Long mid = (Long) httpSession.getAttribute("mid");
        String orderID_session = httpSession.getAttribute("orderId").toString();

        /*
        * 인코딩된 값을 Basic 인증 헤더에 넣고 요청 본문도 추가하세요.
        * 앞 단계에서 리다이렉트 URL로 받은 paymentKey, orderId, amount를 넣어주세요.
        * */
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(secretKey.getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200 ? true : false;
        model.addAttribute("isSuccess", isSuccess);

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();
        model.addAttribute("responseStr", jsonObject.toJSONString());
        System.out.println(jsonObject.toJSONString());

        model.addAttribute("method", (String) jsonObject.get("method"));
        model.addAttribute("orderName", (String) jsonObject.get("orderName"));

        log.info("if앞..." + orderId );
        orderListService.updateOrderListStatus(orderID_session, mid); // totalPrice와 orderNum이 일치하다면 status에 1을 update

        if(orderListService.showOrderListStatus(mid, orderID_session)){ // status의 값을 가져와 참이라면 결제 성공 여부 결정
            log.info("비교결과 true");
            if (((String) jsonObject.get("method")) != null) {
                if (((String) jsonObject.get("method")).equals("카드")) {
                    model.addAttribute("cardNumber", (String) ((JSONObject) jsonObject.get("card")).get("number"));
                } else if (((String) jsonObject.get("method")).equals("가상계좌")) {
                    model.addAttribute("accountNumber", (String) ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber"));
                } else if (((String) jsonObject.get("method")).equals("계좌이체")) {
                    model.addAttribute("bank", (String) ((JSONObject) jsonObject.get("transfer")).get("bank"));
                } else if (((String) jsonObject.get("method")).equals("휴대폰")) {
                    model.addAttribute("customerMobilePhone", (String) ((JSONObject) jsonObject.get("mobilePhone")).get("customerMobilePhone"));
                }
            } else {
                model.addAttribute("code", (String) jsonObject.get("code"));
                model.addAttribute("message", (String) jsonObject.get("message"));
            }
        }else{
            log.info("비교결과 false");

        }

    }

    @GetMapping(value = "fail")
    public String paymentResult(
            Model model,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code
    ) throws Exception {

        model.addAttribute("code", code);
        model.addAttribute("message", message);

        return "fail";
    }

}