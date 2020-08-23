package com.demo.records.controller;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.graph.models.extensions.EmailAddress;
import com.microsoft.graph.models.extensions.ItemBody;
import com.microsoft.graph.models.extensions.Message;
import com.microsoft.graph.models.extensions.Recipient;
import com.microsoft.graph.models.generated.BodyType;
import com.demo.records.aad.AuthHelper;
import com.demo.records.constant.EmailAddressDev;
import com.demo.records.utils.Graph;
import com.demo.records.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    AuthHelper authHelper;

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private HttpServletResponse httpResponse;

    /**
     * send email to developing team, you can edit it in Application.properties
     */

    @ResponseBody
    @PostMapping("/sendEmail")
    public Result sendEmail(@RequestBody Map<String, Object> params) throws Throwable {
//        log.info("/sendEmail/sendEmail: params="+params.toString());
        Message message = new Message();
        message.subject = params.get("subject").toString();
        ItemBody body = new ItemBody();
        //text way to send
        body.contentType = BodyType.TEXT;
        body.content = params.get("content").toString();
        String ccList = params.containsKey("cc") ? params.get("cc").toString() : "";
        //add cc recipients
        LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
        String[] ccs = ccList.split(",");
        for (String cc : ccs) {
            if (cc == null || cc.length() == 0) continue;
            Recipient ccRecipient = new Recipient();
            EmailAddress ccEmailAddress = new EmailAddress();
            ccEmailAddress.address = cc.trim();
            ccRecipient.emailAddress = ccEmailAddress;
            ccRecipientsList.add(ccRecipient);
        }
        //
        message.body = body;
        LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
        Recipient toRecipients = new Recipient();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.address = EmailAddressDev.EMAIL_ADDRESS_DEV;
        toRecipients.emailAddress = emailAddress;
        toRecipientsList.add(toRecipients);
        message.toRecipients = toRecipientsList;
        message.ccRecipients = ccRecipientsList;

        IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(httpRequest, httpResponse);
        String accessToken=result.accessToken();

        // send email
        Graph.sendEmail(accessToken,message);
        return Result.ok();
    }
}
