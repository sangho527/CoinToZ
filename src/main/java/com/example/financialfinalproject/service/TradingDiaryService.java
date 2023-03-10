package com.example.financialfinalproject.service;

import com.example.financialfinalproject.controller.restController.UpbitRestController;
import com.example.financialfinalproject.domain.dto.TradingDiaryDto;
import com.example.financialfinalproject.domain.dto.TradingDiaryListDto;
import com.example.financialfinalproject.domain.entity.TradingDiary;
import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.response.MyCoinCntResponse;
import com.example.financialfinalproject.domain.upbit.exchange.Acount;
import com.example.financialfinalproject.domain.upbit.exchange.OrderDeleteResponse;
import com.example.financialfinalproject.domain.upbit.exchange.OrderOneResponse;
import com.example.financialfinalproject.domain.upbit.exchange.OrderResponse;
import com.example.financialfinalproject.domain.upbit.quotation.Ticker;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.exception.ErrorCode;
import com.example.financialfinalproject.repository.TradingDiaryRepository;
import com.example.financialfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.financialfinalproject.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class TradingDiaryService {

    private final TradingDiaryRepository tradingDiaryRepository;
    private final UserRepository userRepository;


    private String BitCoin = "KRW-BTC";
    private String Ethereum = "KRW-ETH";
    private String Ripple = "KRW-XRP";
    private String ADA = "KRW-ADA";
    private String DogeCoin = "KRW-DOGE";


    public List<TradingDiaryListDto> listOf(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });
        return tradingDiaryRepository.findAllByUserOrderByDate(user)
                .stream().map(TradingDiaryListDto::toDto).collect(Collectors.toList());

    }


    public List<TradingDiaryListDto> findListByCond(String email, String startDate, String endDate) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });
        log.info("{}", LocalDateTime.parse(startDate));
        log.info("{}", LocalDateTime.parse(endDate));

        return tradingDiaryRepository.searchDateRange(user, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate))
                .stream().map(TradingDiaryListDto::toDto).collect(Collectors.toList());

    }

    @Transactional
    public void write(OrderOneResponse orderOneResponse, User user, OrderResponse orderResponse) {


        userRepository.findById(user.getId()).orElseThrow(() -> new AppException(USERNAME_NOT_FOUND, "?????? user ??? ?????? ??? ????????????."));


        // ????????? ??????
        if (orderResponse.getOrd_type().equals("limit")) {

            // ?????? - ????????? ??????, ???????????? ??????
            if (orderResponse.getSide().equals("bid")) {

                TradingDiary tradingDiary = TradingDiary.builder()
                        .ord_type(orderResponse.getOrd_type())
                        .side(orderResponse.getSide())
                        .created_at(LocalDateTime.parse(orderOneResponse.getCreated_at().split("\\+")[0])) // ?????? ????????????
                        .market(orderResponse.getMarket())
                        .price(Double.valueOf(orderResponse.getPrice())) // ????????????(????????? ??????)
                        .volume(Double.valueOf(orderResponse.getVolume())) // ?????? ????????? ????????????
                        .uuid(orderResponse.getUuid())
                        .user(user)
                        .build();

                tradingDiaryRepository.save(tradingDiary);
            }

            // ?????? - ????????? ??????, ???????????? ??????
            else if (orderResponse.getSide().equals("ask")) {

                TradingDiary tradingDiary = TradingDiary.builder()
                        .ord_type(orderResponse.getOrd_type())
                        .side(orderResponse.getSide())
                        .created_at(LocalDateTime.parse(orderResponse.getCreated_at().split("\\+")[0])) // ?????? ????????????
                        .market(orderResponse.getMarket())
                        .price(Double.valueOf(orderResponse.getPrice())) // ????????????(????????? ??????)
                        .volume(Double.valueOf(orderResponse.getVolume())) // ?????? ????????? ????????????
                        .uuid(orderResponse.getUuid())
                        .user(user)
                        .build();

                tradingDiaryRepository.save(tradingDiary);
            }

        }

        if (orderResponse.getOrd_type().equals("market") || orderResponse.getOrd_type().equals("price")) {


            // ?????? - ????????? ?????? (?????? ?????? ????????? ??????)
            if (orderOneResponse.getSide().equals("bid")) {
                log.info("test:{}", orderOneResponse.getExecuted_volume());
                TradingDiary tradingDiary = TradingDiary.builder()
                        .ord_type(orderOneResponse.getOrd_type())
                        .side(orderOneResponse.getSide())
                        .created_at(LocalDateTime.parse(orderOneResponse.getCreated_at().split("\\+")[0])) // ?????? ????????????
                        .market(orderOneResponse.getMarket())
                        .price(Double.valueOf(orderOneResponse.getPrice())) // ????????????
                        .volume(Double.valueOf(orderOneResponse.getExecuted_volume())) // ?????? ??? ?????? ( ????????? ????????? ?????? ??? ?????? ??????)
                        .uuid(orderOneResponse.getUuid())
                        .user(user)
                        .build();

                tradingDiaryRepository.save(tradingDiary);
            }

            // ?????? - ?????? ????????? ??? ????????? ?????? (????????? ??????)

            if (orderOneResponse.getSide().equals("ask")) {
                log.info("test2:{}", orderOneResponse.getTrades().get(0));
                TradingDiary tradingDiary = TradingDiary.builder()
                        .ord_type(orderOneResponse.getOrd_type())
                        .side(orderOneResponse.getSide())
                        .created_at(LocalDateTime.parse(orderOneResponse.getCreated_at().split("\\+")[0])) // ?????? ????????????
                        .market(orderOneResponse.getMarket())
                        .price(orderOneResponse.getTrades().get(0).getFunds()) // ????????????
                        .volume(orderOneResponse.getVolume())
                        .uuid(orderOneResponse.getUuid())// ?????? ??? ?????? ( ????????? ????????? ?????? ??? ?????? ??????
                        .user(user)
                        .build();

                tradingDiaryRepository.save(tradingDiary);
            }
        }

    }


    @Transactional
    public TradingDiaryDto edit(Long id, String comment, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, "?????? user??? ????????? ????????????."));


        TradingDiary tradingDiary = tradingDiaryRepository.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new AppException(DIARY_NOT_FOUND, "?????? ????????? ????????????."));

        tradingDiary.setComment(comment);

        tradingDiaryRepository.save(tradingDiary);

        return TradingDiaryDto.builder()
                .message("????????? ?????????????????????.")
                .build();
    }


    @Transactional
    public TradingDiaryDto delete(String email, Long id) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, "?????? user??? ????????? ????????????."));

        Optional<TradingDiary> tradingDiary = tradingDiaryRepository.findByUserIdAndId(user.getId(), id);
        tradingDiary.orElseThrow(() -> new AppException(ErrorCode.DIARY_NOT_FOUND, "?????? ????????? ????????????."));

        tradingDiaryRepository.deleteById(id);

        return TradingDiaryDto.builder()
                .message("????????? ?????????????????????.")
                .build();
    }

    //????????????
    @Transactional
    public TradingDiaryDto orderDelete(String email, OrderDeleteResponse orderDeleteResponse) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, "?????? user??? ????????? ????????????."));

        tradingDiaryRepository.deleteByUuidAndUserId(orderDeleteResponse.getUuid(), user.getId());

        return TradingDiaryDto.builder()
                .message("????????? ???????????? ????????? ?????????????????????.")
                .build();
    }


}






