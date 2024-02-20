package com.climeet.climeet_backend.domain.retool.gymregistration;

import static com.climeet.climeet_backend.domain.retool.gymregistration.EmailTemplates.APPROVE_EMAIL;

import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.retool.gymregistration.dto.GymRegistrationRequest.PatchGymRegistrationReq;
import com.climeet.climeet_backend.domain.retool.gymregistration.dto.GymRegistrationResponse.GetGymRegistrationsDetailInfo;
import com.climeet.climeet_backend.domain.retool.gymregistration.dto.GymRegistrationResponse.GetGymRegistrationsSimpleInfo;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GymRegistrationService {

    private final GymRegistrationRepository gymRegistrationRepository;
    private final JavaMailSender javaMailSender;

    public List<GetGymRegistrationsSimpleInfo> getGymRegistrations() {
        List<GymRegistration> gymRegistrations = gymRegistrationRepository.findAll();

        return gymRegistrations.stream().map(
                gymRegistration -> GetGymRegistrationsSimpleInfo.toDTO(gymRegistration,
                    gymRegistration.getClimbingGym(), gymRegistration.getManager()))
            .toList();
    }

    public GetGymRegistrationsDetailInfo getGymRegistration(Long gymRegistrationId) {
        GymRegistration gymRegistration = gymRegistrationRepository.findById(gymRegistrationId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_GYM_REGISTRATION));

        return GetGymRegistrationsDetailInfo.toDTO(gymRegistration,
            gymRegistration.getClimbingGym(), gymRegistration.getManager());
    }

    @Transactional
    public void changeGymRegistrations(Long gymRegistrationId,
        PatchGymRegistrationReq patchGymRegistrationReq) throws MessagingException {

        GymRegistration gymRegistration = gymRegistrationRepository.findById(gymRegistrationId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_GYM_REGISTRATION));
        Boolean isApproved = patchGymRegistrationReq.getIsApproved();
        String content = patchGymRegistrationReq.getContent();
        Manager manager = gymRegistration.getManager();

        gymRegistration.updateApprovalStatus(isApproved);

        sendEmail(isApproved, content);
    }

    private void sendEmail(Boolean isApproved, String content) throws MessagingException {
        MimeMessage mail = javaMailSender.createMimeMessage();

        //메일 제목 지정
        mail.setSubject("[클밋] 승인되었어용", "utf-8");
        //메일 내용 지정
        mail.setText(createEmailContent(content), "utf-8", "html");
        //이메일 주소 설정
        mail.addRecipient(RecipientType.TO, new InternetAddress("gourderased@gmail.com"));
        mail.addRecipient(RecipientType.TO, new InternetAddress("bing_01@naver.com"));
        mail.addRecipient(RecipientType.TO, new InternetAddress("ret0422@gmail.com"));
        //이메일 전송
        javaMailSender.send(mail);
    }

    private String createEmailContent(String content) {
        return APPROVE_EMAIL;
    }
}