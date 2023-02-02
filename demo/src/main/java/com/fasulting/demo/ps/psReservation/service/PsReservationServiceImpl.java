package com.fasulting.demo.ps.psReservation.service;

import com.fasulting.demo.common.consulting.repository.ConsultingRepository;
import com.fasulting.demo.common.operatingCal.repository.OperatingCalRepository;
import com.fasulting.demo.common.psOperating.dto.respDto.PsOperatingRespDto;
import com.fasulting.demo.common.psOperating.repository.PsOperatingRepository;
import com.fasulting.demo.common.report.repository.ReportEntityRepository;
import com.fasulting.demo.common.report.repository.ReportRepository;
import com.fasulting.demo.common.reservation.repository.ReservationRepository;
import com.fasulting.demo.common.reservation.repository.ReservationSubRepository;
import com.fasulting.demo.common.time.repository.TimeRepository;
import com.fasulting.demo.common.util.Date2String;
import com.fasulting.demo.entity.*;
import com.fasulting.demo.ps.ps.repository.PsRepository;
import com.fasulting.demo.ps.ps.repository.SubCategoryRepository;
import com.fasulting.demo.ps.psReservation.dto.reqDto.ReservationReqDto;
import com.fasulting.demo.ps.psReservation.dto.respDto.PostReservationRespDto;
import com.fasulting.demo.ps.psReservation.dto.respDto.PreDetailRespDto;
import com.fasulting.demo.ps.psReservation.dto.respDto.PreReservationRespDto;
import com.fasulting.demo.ps.psReservation.dto.respDto.PsPostRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
public class PsReservationServiceImpl implements PsReservationService {
    private final SubCategoryRepository subCategoryRepository;
    private final ReportEntityRepository reportEntityRepository;
    private final ConsultingRepository consultingRepository;

    private final ReservationRepository reservationRepository;
    private final ReservationSubRepository reservationSubEntityRepository;
    private final PsOperatingRepository psOperatingRepository;
    private final PsRepository psRepository;
    private final OperatingCalRepository operatingCalRepository;
    private final TimeRepository timeRepository;
    private final ReservationSubRepository reservationSubRepository;
    private final ReportRepository reportRepository;

    public PsReservationServiceImpl(ReservationRepository reservationRepository,
                                    ReservationSubRepository reservationSubEntityRepository, PsOperatingRepository psOperatingRepository, PsRepository psRepository, OperatingCalRepository operatingCalRepository, TimeRepository timeRepository,
                                    ConsultingRepository consultingRepository,
                                    ReportEntityRepository reportEntityRepository,
                                    SubCategoryRepository subCategoryRepository, ReservationSubRepository reservationSubRepository, ReportRepository reportRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationSubEntityRepository = reservationSubEntityRepository;
        this.psOperatingRepository = psOperatingRepository;
        this.psRepository = psRepository;
        this.operatingCalRepository = operatingCalRepository;
        this.timeRepository = timeRepository;
        this.consultingRepository = consultingRepository;
        this.reportEntityRepository = reportEntityRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.reservationSubRepository = reservationSubRepository;
        this.reportRepository = reportRepository;
    }

    @Transactional
    @Override
    public boolean cancelReservation(ReservationReqDto reservationReqDto) {

        log.info("cancelReservation - call");

        Long rSeq = reservationReqDto.getReservationSeq();
        Long pSeq = reservationReqDto.getPsSeq();

        // 예약 delYn >> Y로 변경
        if (reservationRepository.findById(rSeq).isPresent() && reservationRepository.findById(rSeq).get().getPs().getSeq() == pSeq) {

            ReservationEntity r = reservationRepository.findById(rSeq).get();
            r.updateDelYn();

            reservationRepository.save(r);

            // 예약 취소하고 해당 시간을 해당 병원 운영 시간에 추가

            // 예약에서 병원 구하기
            // 예약에서 date 구하기
            // 예약에서 시간 구하기
            // 병원 date(operating_cal) 시간(time) 으로 운영 시간(ps_operating) 추가

            PsEntity ps = r.getPs();

            OperatingCalEntity op = operatingCalRepository.findByYearAndMonthAndDay(
                            r.getReservationCal().getYear(),
                            r.getReservationCal().getMonth(),
                            r.getReservationCal().getDay())
                    .get();

            TimeEntity time = timeRepository.findById(r.getTime().getSeq()).get();

            PsOperatingEntity psOperating = PsOperatingEntity.builder()
                    .ps(ps)
                    .operatingCal(op)
                    .time(time)
                    .build();

            psOperatingRepository.save(psOperating);

            return true;
        }

        return false;

    }

    @Transactional
    @Override
    public PsPostRespDto getPostReservationList(Long psSeq, LocalDateTime current) {

        // 미래 예약 조회
        List<PostReservationRespDto> respList = new ArrayList<>();

        List<ReservationEntity> rList = reservationRepository.findAllByPs(psRepository.findById(psSeq).get());

        for (ReservationEntity r : rList) {

            if ("N".equals(r.getDelYn()) && !consultingRepository.findByReservation(r).isPresent()) {

                PostReservationRespDto respDto = PostReservationRespDto.builder()
                        .reservationSeq(r.getSeq())
                        .userName(r.getUser().getName())
                        .date(Date2String.Date2String(r.getReservationCal().getYear(),
                                r.getReservationCal().getMonth(),
                                r.getReservationCal().getDay(),
                                r.getReservationCal().getDayOfWeek(),
                                r.getTime().getStartHour(),
                                r.getTime().getStartMin()))
                        .subCategoryName(reservationSubRepository.getSubCategoryNameByReservationSeq(r.getSeq()))
                        .build();

                respList.add(respDto);
            }
        }

        // 운영 시간 조회
        LocalDateTime post = current.plusDays(6);

        Map<String, PsOperatingRespDto> map = new TreeMap<>();

        List<PsOperatingEntity> psOperatingList = psOperatingRepository.getByPsSeqAndCurrent(psSeq, current, post);

        for (PsOperatingEntity po : psOperatingList) {

            Integer year = po.getOperatingCal().getYear();
            Integer month = po.getOperatingCal().getMonth();
            Integer day = po.getOperatingCal().getDay();


            String key = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);

            if (map.get(key) == null) {
                List<Integer> timeList = new ArrayList<>();
                timeList.add(po.getTime().getNum());

                PsOperatingRespDto value = PsOperatingRespDto.builder()
                        .year(year)
                        .month(month)
                        .day(day)
                        .dayOfWeek(po.getOperatingCal().getDayOfWeek())
                        .time(timeList)
                        .build();

                map.put(key, value);
            } else {
                PsOperatingRespDto value = map.get(key);
                value.addTime(po.getTime().getNum());

                map.put(key, value);
            }
        }

        List<PsOperatingRespDto> poList = new ArrayList<>();
        for (Map.Entry<String, PsOperatingRespDto> entrySet : map.entrySet()) {
//            log.info(entrySet.getKey() + " : " + entrySet.getValue());
            poList.add(entrySet.getValue());
        }

        PsPostRespDto postReservationRespDto = PsPostRespDto.builder()
                .reservation(respList)
                .operatingTime(poList)
                .build();

        return postReservationRespDto;
    }

    @Transactional
    @Override
    public List<PreReservationRespDto> getPreReservationList(Long psSeq, LocalDateTime current) {


        List<PreReservationRespDto> respList = new ArrayList<>();

        List<ConsultingEntity> cList = consultingRepository.findAllByPs(psRepository.findById(psSeq).get());

//        log.info(cList.toString());

        for (ConsultingEntity c : cList) {

            log.info(c.toString());

            String estimate = "";

            if (reportRepository.findByConsulting(c).isPresent()) {
                estimate = reportRepository.findByConsulting(c).get().getEstimate();
            }

            PreReservationRespDto respDto = PreReservationRespDto.builder()
                    .consultingSeq(c.getSeq())
                    .userName(c.getUser().getName())
                    .estimate(estimate)
                    .subCategoryName(reservationSubRepository.getSubCategoryNameByReservationSeq(c.getReservation().getSeq()))
                    .date(Date2String.Date2String(c.getReservation().getReservationCal().getYear(),
                            c.getReservation().getReservationCal().getMonth(),
                            c.getReservation().getReservationCal().getDay(),
                            c.getReservation().getReservationCal().getDayOfWeek(),
                            c.getReservation().getTime().getStartHour()))
                    .build();

            respList.add(respDto);
        }


        return respList;
    }

    @Transactional
    @Override
    public PreDetailRespDto getPreDetail(Long consultingSeq) {
        ConsultingEntity consulting = consultingRepository.findById(consultingSeq).get();
        UserEntity user = consulting.getUser();
        ReportEntity report = reportEntityRepository.findByConsulting(consulting);
        ReservationCalEntity reservationCal = consulting.getReservation().getReservationCal();

        PreDetailRespDto preDetail = PreDetailRespDto.builder()
                .userName(user.getName())
                .userNumber(user.getNumber())
                .userBirth(user.getBirth())
                .date(reservationCal.getDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .subCategoryName(reservationSubEntityRepository.getSubCategoryNameByReservationSeq(consulting.getReservation().getSeq()))
                .content(report.getContent())
                .estimate(report.getEstimate())
                .build();

        return preDetail;
    }


}

