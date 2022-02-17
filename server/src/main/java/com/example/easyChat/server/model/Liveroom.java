package com.example.easyChat.server.model;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class Liveroom {
    /**
     * 直播间ID
     */
    private Integer id;

    /**
     * 直播间简介
     */
    private String description;

    /**
     * 主播ID
     */
    private Long userid;

    /**
     * 直播间创建时间
     */
    private Date createdate;

    /**
     * 直播间关闭时间
     */
    private Date closedate;

    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String DESCRIPTION = "description";

    public static final String DB_DESCRIPTION = "description";

    public static final String USERID = "userid";

    public static final String DB_USERID = "userId";

    public static final String CREATEDATE = "createdate";

    public static final String DB_CREATEDATE = "createDate";

    public static final String CLOSEDATE = "closedate";

    public static final String DB_CLOSEDATE = "closeDate";
}