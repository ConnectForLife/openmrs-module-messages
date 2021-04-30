CREATE FUNCTION `GET_PREDICTION_START_DATE_FOR_ADHERENCE_DAILY`(patient_id long, actor_id long, execution_start_date DATETIME)
    RETURNS DATETIME
BEGIN
    DECLARE mssg_date DATETIME;
    SET mssg_date =
            (SELECT EXECUTION_DATE FROM
                (SELECT mssg.msg_send_time AS EXECUTION_DATE, 1 AS MESSAGE_ID,
                        mssg.channel_type AS CHANNEL_ID, mss.status AS STATUS_ID
                 FROM messages_scheduled_service mss
                          JOIN messages_patient_template mpt on mpt.messages_patient_template_id = mss.patient_template_id
                          JOIN messages_template mt on mt.messages_template_id = mpt.template_id
                          JOIN messages_scheduled_service_group mssg on mssg.messages_scheduled_service_group_id = mss.group_id
                 WHERE
                         mt.name = 'Adherence report daily'
                   AND mpt.patient_id = patient_id
                   AND mpt.actor_id = actor_id
                 ORDER BY 1 DESC)
                    a LIMIT 1);
    RETURN if(mssg_date > execution_start_date, mssg_date, execution_start_date);
END
