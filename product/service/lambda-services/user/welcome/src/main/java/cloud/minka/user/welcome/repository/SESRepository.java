package cloud.minka.user.welcome.repository;

import software.amazon.awssdk.services.ses.SesClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SESRepository {

    SesClient sesClient;

    @Inject
    public SESRepository(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    public void sendEmail() {
        sesClient.sendEmail(
                builder -> builder.destination(
                                destination -> destination.toAddresses("diogo.velho@mindera.com"))
                        .message(message -> message
                                .body(body -> body.text(text -> text.data("Hello, world!")))
                                .body(body -> body.html(html -> html.data("<!doctype html>\n" +
                                        "<html ⚡4email data-css-strict>\n" +
                                        "\n" +
                                        "<head>\n" +
                                        "    <meta charset='utf-8'>\n" +
                                        "    <style amp4email-boilerplate>\n" +
                                        "        body {\n" +
                                        "            visibility: hidden\n" +
                                        "        }\n" +
                                        "    </style>\n" +
                                        "    <script async src='https://cdn.ampproject.org/v0.js'></script>\n" +
                                        "    <style amp-custom>\n" +
                                        "        .es-button:hover {\n" +
                                        "            background: #555555;\n" +
                                        "            border-color: #555555;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-desk-hidden {\n" +
                                        "            display: none;\n" +
                                        "            float: left;\n" +
                                        "            overflow: hidden;\n" +
                                        "            width: 0;\n" +
                                        "            max-height: 0;\n" +
                                        "            line-height: 0;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        s {\n" +
                                        "            text-decoration: line-through;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        body {\n" +
                                        "            width: 100%;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        body {\n" +
                                        "            font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        table {\n" +
                                        "            border-collapse: collapse;\n" +
                                        "            border-spacing: 0px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        table td,\n" +
                                        "        html,\n" +
                                        "        body,\n" +
                                        "        .es-wrapper {\n" +
                                        "            padding: 0;\n" +
                                        "            Margin: 0;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-content,\n" +
                                        "        .es-header,\n" +
                                        "        .es-footer {\n" +
                                        "            table-layout: fixed;\n" +
                                        "            width: 100%;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        p,\n" +
                                        "        hr {\n" +
                                        "            Margin: 0;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        h1,\n" +
                                        "        h2,\n" +
                                        "        h3,\n" +
                                        "        h4,\n" +
                                        "        h5 {\n" +
                                        "            Margin: 0;\n" +
                                        "            line-height: 120%;\n" +
                                        "            font-family: lato, 'helvetica neue', helvetica, arial, sans-serif;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-left {\n" +
                                        "            float: left;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-right {\n" +
                                        "            float: right;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p5 {\n" +
                                        "            padding: 5px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p5t {\n" +
                                        "            padding-top: 5px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p5b {\n" +
                                        "            padding-bottom: 5px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p5l {\n" +
                                        "            padding-left: 5px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p5r {\n" +
                                        "            padding-right: 5px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p10 {\n" +
                                        "            padding: 10px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p10t {\n" +
                                        "            padding-top: 10px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p10b {\n" +
                                        "            padding-bottom: 10px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p10l {\n" +
                                        "            padding-left: 10px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p10r {\n" +
                                        "            padding-right: 10px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p15 {\n" +
                                        "            padding: 15px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p15t {\n" +
                                        "            padding-top: 15px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p15b {\n" +
                                        "            padding-bottom: 15px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p15l {\n" +
                                        "            padding-left: 15px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p15r {\n" +
                                        "            padding-right: 15px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p20 {\n" +
                                        "            padding: 20px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p20t {\n" +
                                        "            padding-top: 20px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p20b {\n" +
                                        "            padding-bottom: 20px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p20l {\n" +
                                        "            padding-left: 20px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p20r {\n" +
                                        "            padding-right: 20px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p25 {\n" +
                                        "            padding: 25px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p25t {\n" +
                                        "            padding-top: 25px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p25b {\n" +
                                        "            padding-bottom: 25px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p25l {\n" +
                                        "            padding-left: 25px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p25r {\n" +
                                        "            padding-right: 25px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p30 {\n" +
                                        "            padding: 30px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p30t {\n" +
                                        "            padding-top: 30px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p30b {\n" +
                                        "            padding-bottom: 30px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p30l {\n" +
                                        "            padding-left: 30px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p30r {\n" +
                                        "            padding-right: 30px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p35 {\n" +
                                        "            padding: 35px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p35t {\n" +
                                        "            padding-top: 35px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p35b {\n" +
                                        "            padding-bottom: 35px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p35l {\n" +
                                        "            padding-left: 35px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p35r {\n" +
                                        "            padding-right: 35px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p40 {\n" +
                                        "            padding: 40px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p40t {\n" +
                                        "            padding-top: 40px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p40b {\n" +
                                        "            padding-bottom: 40px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p40l {\n" +
                                        "            padding-left: 40px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p40r {\n" +
                                        "            padding-right: 40px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-menu td {\n" +
                                        "            border: 0;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        a {\n" +
                                        "            text-decoration: underline;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        h1 a {\n" +
                                        "            text-align: center;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        h2 a {\n" +
                                        "            text-align: left;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        h3 a {\n" +
                                        "            text-align: left;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        p,\n" +
                                        "        ul li,\n" +
                                        "        ol li {\n" +
                                        "            font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif;\n" +
                                        "            line-height: 150%;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        ul li,\n" +
                                        "        ol li {\n" +
                                        "            Margin-bottom: 15px;\n" +
                                        "            margin-left: 0;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-menu td a {\n" +
                                        "            text-decoration: none;\n" +
                                        "            display: block;\n" +
                                        "            font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-menu amp-img,\n" +
                                        "        .es-button amp-img {\n" +
                                        "            vertical-align: middle;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-wrapper {\n" +
                                        "            width: 100%;\n" +
                                        "            height: 100%;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-wrapper-color,\n" +
                                        "        .es-wrapper {\n" +
                                        "            background-color: #F1F1F1;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-header {\n" +
                                        "            background-color: transparent;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-header-body {\n" +
                                        "            background-color: #333333;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-header-body p,\n" +
                                        "        .es-header-body ul li,\n" +
                                        "        .es-header-body ol li {\n" +
                                        "            color: #FFFFFF;\n" +
                                        "            font-size: 14px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-header-body a {\n" +
                                        "            color: #FFFFFF;\n" +
                                        "            font-size: 14px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-content-body {\n" +
                                        "            background-color: #FFFFFF;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-content-body p,\n" +
                                        "        .es-content-body ul li,\n" +
                                        "        .es-content-body ol li {\n" +
                                        "            color: #555555;\n" +
                                        "            font-size: 15px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-content-body a {\n" +
                                        "            color: #26A4D3;\n" +
                                        "            font-size: 15px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-footer {\n" +
                                        "            background-color: transparent;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-footer-body {\n" +
                                        "            background-color: #FFFFFF;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-footer-body p,\n" +
                                        "        .es-footer-body ul li,\n" +
                                        "        .es-footer-body ol li {\n" +
                                        "            color: #666666;\n" +
                                        "            font-size: 12px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-footer-body a {\n" +
                                        "            color: #666666;\n" +
                                        "            font-size: 12px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-infoblock,\n" +
                                        "        .es-infoblock p,\n" +
                                        "        .es-infoblock ul li,\n" +
                                        "        .es-infoblock ol li {\n" +
                                        "            line-height: 120%;\n" +
                                        "            font-size: 12px;\n" +
                                        "            color: #CCCCCC;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-infoblock a {\n" +
                                        "            font-size: 12px;\n" +
                                        "            color: #CCCCCC;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        h1 {\n" +
                                        "            font-size: 30px;\n" +
                                        "            font-style: normal;\n" +
                                        "            font-weight: bold;\n" +
                                        "            color: #333333;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        h2 {\n" +
                                        "            font-size: 20px;\n" +
                                        "            font-style: normal;\n" +
                                        "            font-weight: bold;\n" +
                                        "            color: #333333;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        h3 {\n" +
                                        "            font-size: 18px;\n" +
                                        "            font-style: normal;\n" +
                                        "            font-weight: normal;\n" +
                                        "            color: #333333;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-header-body h1 a,\n" +
                                        "        .es-content-body h1 a,\n" +
                                        "        .es-footer-body h1 a {\n" +
                                        "            font-size: 30px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-header-body h2 a,\n" +
                                        "        .es-content-body h2 a,\n" +
                                        "        .es-footer-body h2 a {\n" +
                                        "            font-size: 20px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-header-body h3 a,\n" +
                                        "        .es-content-body h3 a,\n" +
                                        "        .es-footer-body h3 a {\n" +
                                        "            font-size: 18px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        a.es-button,\n" +
                                        "        button.es-button {\n" +
                                        "            border-style: solid;\n" +
                                        "            border-color: #26A4D3;\n" +
                                        "            border-width: 15px 30px 15px 30px;\n" +
                                        "            display: inline-block;\n" +
                                        "            background: #26A4D3;\n" +
                                        "            border-radius: 50px;\n" +
                                        "            font-size: 14px;\n" +
                                        "            font-family: arial, 'helvetica neue', helvetica, sans-serif;\n" +
                                        "            font-weight: bold;\n" +
                                        "            font-style: normal;\n" +
                                        "            line-height: 120%;\n" +
                                        "            color: #FFFFFF;\n" +
                                        "            text-decoration: none;\n" +
                                        "            width: auto;\n" +
                                        "            text-align: center;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-button-border {\n" +
                                        "            border-style: solid solid solid solid;\n" +
                                        "            border-color: #26A4D3 #26A4D3 #26A4D3 #26A4D3;\n" +
                                        "            background: #26A4D3;\n" +
                                        "            border-width: 0px 0px 0px 0px;\n" +
                                        "            display: inline-block;\n" +
                                        "            border-radius: 50px;\n" +
                                        "            width: auto;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p-default {\n" +
                                        "            padding-top: 20px;\n" +
                                        "            padding-right: 40px;\n" +
                                        "            padding-bottom: 0px;\n" +
                                        "            padding-left: 40px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        .es-p-all-default {\n" +
                                        "            padding: 0px;\n" +
                                        "        }\n" +
                                        "\n" +
                                        "        @media only screen and (max-width:600px) {\n" +
                                        "\n" +
                                        "            p,\n" +
                                        "            ul li,\n" +
                                        "            ol li,\n" +
                                        "            a {\n" +
                                        "                line-height: 150%\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            h1,\n" +
                                        "            h2,\n" +
                                        "            h3,\n" +
                                        "            h1 a,\n" +
                                        "            h2 a,\n" +
                                        "            h3 a {\n" +
                                        "                line-height: 120%\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            h1 {\n" +
                                        "                font-size: 30px;\n" +
                                        "                text-align: center\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            h2 {\n" +
                                        "                font-size: 26px;\n" +
                                        "                text-align: left\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            h3 {\n" +
                                        "                font-size: 20px;\n" +
                                        "                text-align: left\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            h1 a {\n" +
                                        "                text-align: center\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-header-body h1 a,\n" +
                                        "            .es-content-body h1 a,\n" +
                                        "            .es-footer-body h1 a {\n" +
                                        "                font-size: 30px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            h2 a {\n" +
                                        "                text-align: left\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-header-body h2 a,\n" +
                                        "            .es-content-body h2 a,\n" +
                                        "            .es-footer-body h2 a {\n" +
                                        "                font-size: 20px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            h3 a {\n" +
                                        "                text-align: left\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-header-body h3 a,\n" +
                                        "            .es-content-body h3 a,\n" +
                                        "            .es-footer-body h3 a {\n" +
                                        "                font-size: 20px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-menu td a {\n" +
                                        "                font-size: 16px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-header-body p,\n" +
                                        "            .es-header-body ul li,\n" +
                                        "            .es-header-body ol li,\n" +
                                        "            .es-header-body a {\n" +
                                        "                font-size: 16px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-content-body p,\n" +
                                        "            .es-content-body ul li,\n" +
                                        "            .es-content-body ol li,\n" +
                                        "            .es-content-body a {\n" +
                                        "                font-size: 17px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-footer-body p,\n" +
                                        "            .es-footer-body ul li,\n" +
                                        "            .es-footer-body ol li,\n" +
                                        "            .es-footer-body a {\n" +
                                        "                font-size: 17px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-infoblock p,\n" +
                                        "            .es-infoblock ul li,\n" +
                                        "            .es-infoblock ol li,\n" +
                                        "            .es-infoblock a {\n" +
                                        "                font-size: 12px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            *[class='gmail-fix'] {\n" +
                                        "                display: none\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-m-txt-c,\n" +
                                        "            .es-m-txt-c h1,\n" +
                                        "            .es-m-txt-c h2,\n" +
                                        "            .es-m-txt-c h3 {\n" +
                                        "                text-align: center\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-m-txt-r,\n" +
                                        "            .es-m-txt-r h1,\n" +
                                        "            .es-m-txt-r h2,\n" +
                                        "            .es-m-txt-r h3 {\n" +
                                        "                text-align: right\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-m-txt-l,\n" +
                                        "            .es-m-txt-l h1,\n" +
                                        "            .es-m-txt-l h2,\n" +
                                        "            .es-m-txt-l h3 {\n" +
                                        "                text-align: left\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-m-txt-r amp-img {\n" +
                                        "                float: right\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-m-txt-c amp-img {\n" +
                                        "                margin: 0 auto\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-m-txt-l amp-img {\n" +
                                        "                float: left\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-button-border {\n" +
                                        "                display: inline-block\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            a.es-button,\n" +
                                        "            button.es-button {\n" +
                                        "                font-size: 14px;\n" +
                                        "                display: inline-block;\n" +
                                        "                border-width: 15px 25px 15px 25px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-btn-fw {\n" +
                                        "                border-width: 10px 0px;\n" +
                                        "                text-align: center\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-adaptive table,\n" +
                                        "            .es-btn-fw,\n" +
                                        "            .es-btn-fw-brdr,\n" +
                                        "            .es-left,\n" +
                                        "            .es-right {\n" +
                                        "                width: 100%\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-content table,\n" +
                                        "            .es-header table,\n" +
                                        "            .es-footer table,\n" +
                                        "            .es-content,\n" +
                                        "            .es-footer,\n" +
                                        "            .es-header {\n" +
                                        "                width: 100%;\n" +
                                        "                max-width: 600px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-adapt-td {\n" +
                                        "                display: block;\n" +
                                        "                width: 100%\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .adapt-img {\n" +
                                        "                width: 100%;\n" +
                                        "                height: auto\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            td.es-m-p0 {\n" +
                                        "                padding: 0px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            td.es-m-p0r {\n" +
                                        "                padding-right: 0px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            td.es-m-p0l {\n" +
                                        "                padding-left: 0px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            td.es-m-p0t {\n" +
                                        "                padding-top: 0px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            td.es-m-p0b {\n" +
                                        "                padding-bottom: 0\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            td.es-m-p20b {\n" +
                                        "                padding-bottom: 20px\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-mobile-hidden,\n" +
                                        "            .es-hidden {\n" +
                                        "                display: none\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            tr.es-desk-hidden,\n" +
                                        "            td.es-desk-hidden,\n" +
                                        "            table.es-desk-hidden {\n" +
                                        "                width: auto;\n" +
                                        "                overflow: visible;\n" +
                                        "                float: none;\n" +
                                        "                max-height: inherit;\n" +
                                        "                line-height: inherit\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            tr.es-desk-hidden {\n" +
                                        "                display: table-row\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            table.es-desk-hidden {\n" +
                                        "                display: table\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            td.es-desk-menu-hidden {\n" +
                                        "                display: table-cell\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-menu td {\n" +
                                        "                width: 1%\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            table.es-table-not-adapt,\n" +
                                        "            .esd-block-html table {\n" +
                                        "                width: auto\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            table.es-social {\n" +
                                        "                display: inline-block\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            table.es-social td {\n" +
                                        "                display: inline-block\n" +
                                        "            }\n" +
                                        "\n" +
                                        "            .es-desk-hidden {\n" +
                                        "                display: table-row;\n" +
                                        "                width: auto;\n" +
                                        "                overflow: visible;\n" +
                                        "                max-height: inherit\n" +
                                        "            }\n" +
                                        "        }\n" +
                                        "    </style>\n" +
                                        "</head>\n" +
                                        "\n" +
                                        "<body>\n" +
                                        "    <div class='es-wrapper-color'>\n" +
                                        "        <!--[if gte mso 9]><v:background xmlns:v='urn:schemas-microsoft-com:vml' fill='t'> <v:fill type='tile' color='#f1f1f1'></v:fill> </v:background><![endif]-->\n" +
                                        "        <table class='es-wrapper' width='100%' cellspacing='0' cellpadding='0'>\n" +
                                        "            <tr>\n" +
                                        "                <td valign='top'>\n" +
                                        "                    <table cellpadding='0' cellspacing='0' class='es-content' align='center'>\n" +
                                        "                        <tr>\n" +
                                        "                            <td align='center'>\n" +
                                        "                                <table class='es-content-body' style='background-color: transparent' width='600'\n" +
                                        "                                    cellspacing='0' cellpadding='0' align='center'>\n" +
                                        "                                    <tr>\n" +
                                        "                                        <td class='es-p15t es-p15b es-p10r es-p10l' align='left'>\n" +
                                        "                                            <!--[if mso]><table width='580' cellpadding='0' cellspacing='0'><tr><td width='282' valign='top'><![endif]-->\n" +
                                        "                                            <table class='es-left' cellspacing='0' cellpadding='0' align='left'>\n" +
                                        "                                                <tr>\n" +
                                        "                                                    <td width='282' align='left'>\n" +
                                        "                                                        <table width='100%' cellspacing='0' cellpadding='0'\n" +
                                        "                                                            role='presentation'>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-infoblock es-m-txt-c' align='left'>\n" +
                                        "                                                                    <p\n" +
                                        "                                                                        style='font-family: arial, helvetica\\ neue, helvetica, sans-serif'>\n" +
                                        "                                                                        minka.cloud welcome email</p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                        </table>\n" +
                                        "                                                    </td>\n" +
                                        "                                                </tr>\n" +
                                        "                                            </table>\n" +
                                        "                                            <!--[if mso]></td><td width='20'></td><td width='278' valign='top'><![endif]-->\n" +
                                        "                                            <table class='es-right' cellspacing='0' cellpadding='0' align='right'>\n" +
                                        "                                                <tr>\n" +
                                        "                                                    <td width='278' align='left'>\n" +
                                        "                                                        <table width='100%' cellspacing='0' cellpadding='0'\n" +
                                        "                                                            role='presentation'>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td align='right' class='es-infoblock es-m-txt-c'>\n" +
                                        "                                                                    <p><a href='https://minka.cloud/' class='view'\n" +
                                        "                                                                            target='_blank'\n" +
                                        "                                                                            style='font-family: 'arial', 'helvetica neue', 'helvetica', 'sans-serif''>View\n" +
                                        "                                                                            in browser</a></p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                        </table>\n" +
                                        "                                                    </td>\n" +
                                        "                                                </tr>\n" +
                                        "                                            </table>\n" +
                                        "                                            <!--[if mso]></td></tr></table><![endif]-->\n" +
                                        "                                        </td>\n" +
                                        "                                    </tr>\n" +
                                        "                                </table>\n" +
                                        "                            </td>\n" +
                                        "                        </tr>\n" +
                                        "                    </table>\n" +
                                        "                    <table cellpadding='0' cellspacing='0' class='es-header' align='center'>\n" +
                                        "                        <tr>\n" +
                                        "                            <td align='center'>\n" +
                                        "                                <table class='es-header-body' style='background-color: #ffffff' width='600'\n" +
                                        "                                    cellspacing='0' cellpadding='0' bgcolor='#ffffff' align='center'>\n" +
                                        "                                    <tr>\n" +
                                        "                                        <td class='es-p30t es-p30b es-p40r es-p40l' style='background-color: #3d85c6'\n" +
                                        "                                            bgcolor='#3d85c6' align='left'>\n" +
                                        "                                            <table width='100%' cellspacing='0' cellpadding='0'>\n" +
                                        "                                                <tr>\n" +
                                        "                                                    <td width='520' valign='top' align='center'>\n" +
                                        "                                                        <table width='100%' cellspacing='0' cellpadding='0'\n" +
                                        "                                                            role='presentation'>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td align='center' style='font-size: 0px'><a\n" +
                                        "                                                                        href='https://dev1.auth.minka.cloud/ALL/20221031214518/assets/images/image.jpg'\n" +
                                        "                                                                        target='_blank'>\n" +
                                        "                                                                        <amp-img src='https://dev1.auth.minka.cloud/ALL/20221031214518/assets/images/image.jpg' alt\n" +
                                        "                                                                            style='display: block' width='262'\n" +
                                        "                                                                            height='61'></amp-img>\n" +
                                        "                                                                    </a></td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                        </table>\n" +
                                        "                                                    </td>\n" +
                                        "                                                </tr>\n" +
                                        "                                            </table>\n" +
                                        "                                        </td>\n" +
                                        "                                    </tr>\n" +
                                        "                                </table>\n" +
                                        "                            </td>\n" +
                                        "                        </tr>\n" +
                                        "                    </table>\n" +
                                        "                    <table class='es-content' cellspacing='0' cellpadding='0' align='center'>\n" +
                                        "                        <tr>\n" +
                                        "                            <td align='center'>\n" +
                                        "                                <table class='es-content-body' style='background-color: #3d85c6' width='600'\n" +
                                        "                                    cellspacing='0' cellpadding='0' bgcolor='#3d85c6' align='center'>\n" +
                                        "                                    <tr>\n" +
                                        "                                        <td class='es-p40t es-p40b es-p40r es-p40l'\n" +
                                        "                                            style='background-image: url(https://cdn2.vectorstock.com/i/1000x1000/32/46/flat-design-cute-blue-sky-with-clouds-pattern-vector-8833246.jpg);background-repeat: no-repeat;background-position: center top'\n" +
                                        "                                            align='left'>\n" +
                                        "                                            <table width='100%' cellspacing='0' cellpadding='0'>\n" +
                                        "                                                <tr>\n" +
                                        "                                                    <td width='520' valign='top' align='center'>\n" +
                                        "                                                        <table width='100%' cellspacing='0' cellpadding='0'\n" +
                                        "                                                            role='presentation'>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td align='center' class='es-p40t es-p10b'>\n" +
                                        "                                                                    <h1 style='color: #666666'>WELCOME</h1>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-p10t es-p20b es-p30r es-p30l'\n" +
                                        "                                                                    align='center'>\n" +
                                        "                                                                    <p style='color: #666666'>Our inventory app is\n" +
                                        "                                                                        revolutionizing the way people manage their\n" +
                                        "                                                                        inventory</p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-p10t es-p20b' align='center'><span\n" +
                                        "                                                                        class='es-button-border'\n" +
                                        "                                                                        style='border-width: 0px;border-style: solid;background: #38761d;border-color: #3d85c6'><a\n" +
                                        "                                                                            href='https://minka.cloud/'\n" +
                                        "                                                                            class='es-button' target='_blank'\n" +
                                        "                                                                            style='background: #38761d;border-color: #38761d'>ACCESS\n" +
                                        "                                                                            ACCOUNT</a></span></td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                        </table>\n" +
                                        "                                                    </td>\n" +
                                        "                                                </tr>\n" +
                                        "                                            </table>\n" +
                                        "                                        </td>\n" +
                                        "                                    </tr>\n" +
                                        "                                </table>\n" +
                                        "                            </td>\n" +
                                        "                        </tr>\n" +
                                        "                    </table>\n" +
                                        "                    <table class='es-content' cellspacing='0' cellpadding='0' align='center'>\n" +
                                        "                        <tr>\n" +
                                        "                            <td align='center'>\n" +
                                        "                                <table class='es-content-body' width='600' cellspacing='0' cellpadding='0'\n" +
                                        "                                    bgcolor='#ffffff' align='center'>\n" +
                                        "                                    <tr>\n" +
                                        "                                        <td class='es-p40t es-p40r es-p40l' align='left'>\n" +
                                        "                                            <table width='100%' cellspacing='0' cellpadding='0'>\n" +
                                        "                                                <tr>\n" +
                                        "                                                    <td width='520' valign='top' align='center'>\n" +
                                        "                                                        <table width='100%' cellspacing='0' cellpadding='0'\n" +
                                        "                                                            role='presentation'>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-p5t es-p15b es-m-txt-c' align='left'>\n" +
                                        "                                                                    <h2>YOUR ACCOUNT IS NOW ACTIVE</h2>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-p10b' align='left'>\n" +
                                        "                                                                    <p><strong>Welcome to Minka! Our inventory app is\n" +
                                        "                                                                            revolutionizing the way people manage their\n" +
                                        "                                                                            inventory. With Minka, you can add your\n" +
                                        "                                                                            inventory items in a snap. To get started,\n" +
                                        "                                                                            scan a barcode and we'll take it from\n" +
                                        "                                                                            there</strong></p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-p10t es-p10b' align='left'>\n" +
                                        "                                                                    <p>If you have any questions, our customer support\n" +
                                        "                                                                        team is always here to help. You can reach us by\n" +
                                        "                                                                        email at support@minka.com or by phone\n" +
                                        "                                                                        at1-800-123-4567..<br></p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-p10t es-p10b' align='left'>\n" +
                                        "                                                                    <p>Yours sincerely,</p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                        </table>\n" +
                                        "                                                    </td>\n" +
                                        "                                                </tr>\n" +
                                        "                                            </table>\n" +
                                        "                                        </td>\n" +
                                        "                                    </tr>\n" +
                                        "                                    <tr>\n" +
                                        "                                        <td class='es-p10t es-p40b es-p40r es-p40l' align='left'>\n" +
                                        "                                            <!--[if mso]><table width='520' cellpadding='0' cellspacing='0'><tr><td width='40' valign='top'><![endif]-->\n" +
                                        "                                            <table class='es-left' cellspacing='0' cellpadding='0' align='left'>\n" +
                                        "                                                <tr>\n" +
                                        "                                                    <td class='es-m-p0r es-m-p20b' width='40' valign='top'\n" +
                                        "                                                        align='center'>\n" +
                                        "                                                        <table width='100%' cellspacing='0' cellpadding='0'\n" +
                                        "                                                            role='presentation'>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td align='left' style='font-size:0'>\n" +
                                        "                                                                    <amp-img src='https://media-exp1.licdn.com/dms/image/C5603AQHXbOO4SC2spw/profile-displayphoto-shrink_100_100/0/1522331231073?e=1672876800&v=beta&t=Upv_aFiWWuOUYrmtIqy1rfOXBmdaIUBaZq9aN8xLpto' alt\n" +
                                        "                                                                        style='display: block' width='40' height='40'>\n" +
                                        "                                                                    </amp-img>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                        </table>\n" +
                                        "                                                    </td>\n" +
                                        "                                                </tr>\n" +
                                        "                                            </table>\n" +
                                        "                                            <!--[if mso]></td><td width='20'></td><td width='460' valign='top'><![endif]-->\n" +
                                        "                                            <table cellspacing='0' cellpadding='0' align='right'>\n" +
                                        "                                                <tr>\n" +
                                        "                                                    <td width='460' align='left'>\n" +
                                        "                                                        <table width='100%' cellspacing='0' cellpadding='0'\n" +
                                        "                                                            role='presentation'>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-p10t' align='left'>\n" +
                                        "                                                                    <p style='color: #222222;font-size: 14px'>\n" +
                                        "                                                                        <strong>Diogo Velho</strong><br>\n" +
                                        "                                                                    </p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td align='left'>\n" +
                                        "                                                                    <p style='color: #666666;font-size: 14px'>DM |\n" +
                                        "                                                                        Vision</p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                        </table>\n" +
                                        "                                                    </td>\n" +
                                        "                                                </tr>\n" +
                                        "                                            </table>\n" +
                                        "                                            <!--[if mso]></td></tr></table><![endif]-->\n" +
                                        "                                        </td>\n" +
                                        "                                    </tr>\n" +
                                        "                                </table>\n" +
                                        "                            </td>\n" +
                                        "                        </tr>\n" +
                                        "                    </table>\n" +
                                        "                    <table class='es-content' cellspacing='0' cellpadding='0' align='center'>\n" +
                                        "                        <tr>\n" +
                                        "                            <td align='center'>\n" +
                                        "                                <table class='es-content-body' style='background-color: #26a4d3' width='600'\n" +
                                        "                                    cellspacing='0' cellpadding='0' bgcolor='#26a4d3' align='center'>\n" +
                                        "                                    <tr>\n" +
                                        "                                        <td class='es-p40t es-p20b es-p40r es-p40l' style='background-color: #26a4d3'\n" +
                                        "                                            bgcolor='#26a4d3' align='left'>\n" +
                                        "                                            <table width='100%' cellspacing='0' cellpadding='0'>\n" +
                                        "                                                <tr>\n" +
                                        "                                                    <td width='520' valign='top' align='center'>\n" +
                                        "                                                        <table width='100%' cellspacing='0' cellpadding='0'\n" +
                                        "                                                            role='presentation'>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-m-txt-c' align='center'>\n" +
                                        "                                                                    <h2 style='color: #ffffff'>YOUR FEEDBACK IS\n" +
                                        "                                                                        IMPORTANT<br></h2>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-p5t es-p10b' align='center'>\n" +
                                        "                                                                    <p style='color: #aad4ea;font-size: 17px'>Let us\n" +
                                        "                                                                        know what you think of our latest updates<br>\n" +
                                        "                                                                    </p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-p10' align='center'><span\n" +
                                        "                                                                        class='es-button-border'\n" +
                                        "                                                                        style='background: #ffffff '><a\n" +
                                        "                                                                            href='https://minka.cloud/'\n" +
                                        "                                                                            class='es-button es-button-1'\n" +
                                        "                                                                            target='_blank'\n" +
                                        "                                                                            style='background: #ffffff ;border-color: #ffffff;color: #26a4d3;border-width: 15px 25px'>GIVE\n" +
                                        "                                                                            FEEDBACK</a></span></td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                        </table>\n" +
                                        "                                                    </td>\n" +
                                        "                                                </tr>\n" +
                                        "                                            </table>\n" +
                                        "                                        </td>\n" +
                                        "                                    </tr>\n" +
                                        "                                </table>\n" +
                                        "                            </td>\n" +
                                        "                        </tr>\n" +
                                        "                    </table>\n" +
                                        "                    <table class='es-content' cellspacing='0' cellpadding='0' align='center'>\n" +
                                        "                        <tr>\n" +
                                        "                            <td align='center'>\n" +
                                        "                                <table class='es-content-body' style='background-color: #292828' width='600'\n" +
                                        "                                    cellspacing='0' cellpadding='0' bgcolor='#292828' align='center'>\n" +
                                        "                                    <tr>\n" +
                                        "                                        <td class='es-p30t es-p30b es-p40r es-p40l' align='left'>\n" +
                                        "                                            <table width='100%' cellspacing='0' cellpadding='0'>\n" +
                                        "                                                <tr>\n" +
                                        "                                                    <td width='520' valign='top' align='center'>\n" +
                                        "                                                        <table width='100%' cellspacing='0' cellpadding='0'\n" +
                                        "                                                            role='presentation'>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td align='center' style='font-size:0'>\n" +
                                        "                                                                    <table class='es-table-not-adapt es-social'\n" +
                                        "                                                                        cellspacing='0' cellpadding='0'\n" +
                                        "                                                                        role='presentation'>\n" +
                                        "                                                                        <tr>\n" +
                                        "                                                                            <td class='es-p10r' valign='top'\n" +
                                        "                                                                                align='center'>\n" +
                                        "                                                                                <amp-img title='Facebook'\n" +
                                        "                                                                                    src='images/facebook-circle-white.png'\n" +
                                        "                                                                                    alt='Fb' width='24' height='24'>\n" +
                                        "                                                                                </amp-img>\n" +
                                        "                                                                            </td>\n" +
                                        "                                                                            <td class='es-p10r' valign='top'\n" +
                                        "                                                                                align='center'>\n" +
                                        "                                                                                <amp-img title='Twitter'\n" +
                                        "                                                                                    src='images/twitter-circle-white.png'\n" +
                                        "                                                                                    alt='Tw' width='24' height='24'>\n" +
                                        "                                                                                </amp-img>\n" +
                                        "                                                                            </td>\n" +
                                        "                                                                            <td class='es-p10r' valign='top'\n" +
                                        "                                                                                align='center'>\n" +
                                        "                                                                                <amp-img title='Instagram'\n" +
                                        "                                                                                    src='images/instagram-circle-white.png'\n" +
                                        "                                                                                    alt='Inst' width='24' height='24'>\n" +
                                        "                                                                                </amp-img>\n" +
                                        "                                                                            </td>\n" +
                                        "                                                                            <td valign='top' align='center'>\n" +
                                        "                                                                                <amp-img title='Linkedin'\n" +
                                        "                                                                                    src='images/linkedin-circle-white.png'\n" +
                                        "                                                                                    alt='In' width='24' height='24'>\n" +
                                        "                                                                                </amp-img>\n" +
                                        "                                                                            </td>\n" +
                                        "                                                                        </tr>\n" +
                                        "                                                                    </table>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                        </table>\n" +
                                        "                                                    </td>\n" +
                                        "                                                </tr>\n" +
                                        "                                            </table>\n" +
                                        "                                        </td>\n" +
                                        "                                    </tr>\n" +
                                        "                                </table>\n" +
                                        "                            </td>\n" +
                                        "                        </tr>\n" +
                                        "                    </table>\n" +
                                        "                    <table cellpadding='0' cellspacing='0' class='es-footer' align='center'>\n" +
                                        "                        <tr>\n" +
                                        "                            <td align='center'>\n" +
                                        "                                <table class='es-footer-body' style='background-color: #ffffff' width='600'\n" +
                                        "                                    cellspacing='0' cellpadding='0' bgcolor='#ffffff' align='center'>\n" +
                                        "                                    <tr>\n" +
                                        "                                        <td class='es-p40t es-p40b es-p40r es-p40l' align='left'>\n" +
                                        "                                            <table width='100%' cellspacing='0' cellpadding='0'>\n" +
                                        "                                                <tr>\n" +
                                        "                                                    <td width='520' valign='top' align='center'>\n" +
                                        "                                                        <table width='100%' cellspacing='0' cellpadding='0'\n" +
                                        "                                                            role='presentation'>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-p10b' align='center'>\n" +
                                        "                                                                    <p>Company Address</p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td class='es-p10b' align='center'>\n" +
                                        "                                                                    <p>This email was sent to you from Company Email\n" +
                                        "                                                                        Address</p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td align='center' class='es-p10b'>\n" +
                                        "                                                                    <p><a target='_blank'\n" +
                                        "                                                                            href='https://viewstripo.email/'>Preferences</a>\n" +
                                        "                                                                        | <a target='_blank'\n" +
                                        "                                                                            href='https://viewstripo.email/'>Browser</a>\n" +
                                        "                                                                        | <a target='_blank'\n" +
                                        "                                                                            href='https://viewstripo.email/'>Forward</a>\n" +
                                        "                                                                        | <a target='_blank' class='unsubscribe'\n" +
                                        "                                                                            href=''>Unsubscribe</a></p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                            <tr>\n" +
                                        "                                                                <td align='center'>\n" +
                                        "                                                                    <p>Copyright © 2015-2018 <strong>Company\n" +
                                        "                                                                            Name</strong>, All Rights Reserved.</p>\n" +
                                        "                                                                </td>\n" +
                                        "                                                            </tr>\n" +
                                        "                                                        </table>\n" +
                                        "                                                    </td>\n" +
                                        "                                                </tr>\n" +
                                        "                                            </table>\n" +
                                        "                                        </td>\n" +
                                        "                                    </tr>\n" +
                                        "                                </table>\n" +
                                        "                            </td>\n" +
                                        "                        </tr>\n" +
                                        "                    </table>\n" +
                                        "                   \n" +
                                        "                </td>\n" +
                                        "            </tr>\n" +
                                        "        </table>\n" +
                                        "    </div>\n" +
                                        "</body>\n" +
                                        "\n" +
                                        "</html>")))

                                .subject(subject -> subject.data("Welcome to minka.cloud &#127752;")))
                        .source("info@minka.cloud")
        );
        //test

    }
}
