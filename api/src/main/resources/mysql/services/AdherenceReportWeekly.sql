UPDATE messages_template SET service_query =
    'SELECT EXECUTION_DATE,
     MESSAGE_ID,
     CHANNEL_ID,
     null AS STATUS_ID
    FROM
    (
     SELECT TIMESTAMP(selected_date, :bestContactTime) AS EXECUTION_DATE,
         1 AS MESSAGE_ID,
         :Service_type AS CHANNEL_ID
     FROM (
         SELECT *
         FROM (SELECT * FROM DATES_LIST_10_DAYS) v
         WHERE selected_date <= DATE(:endDateTime)
         AND selected_date >= DATE(:startDateTime)
         AND :Week_day_of_delivering_message
         LIKE concat(\'%\',DAYNAME(selected_date),\'%\')
     ) dates
    ) temp
    WHERE EXECUTION_DATE > GET_PREDICTION_START_DATE_FOR_ADHERENCE_WEEKLY(:patientId, :actorId, :executionStartDateTime)
     AND EXECUTION_DATE <= :endDateTime
     AND EXECUTION_DATE >= :startDateTime
     AND CHANNEL_ID != \'Deactivate service\'
    UNION
     SELECT mssg.msg_send_time AS EXECUTION_DATE,
         1 AS MESSAGE_ID,
         mss.channel_type AS CHANNEL_ID,
         mss.status AS STATUS_ID
     FROM messages_scheduled_service mss
         JOIN messages_patient_template mpt on mpt.messages_patient_template_id = mss.patient_template_id
         JOIN messages_template mt on mt.messages_template_id = mpt.template_id
         JOIN messages_scheduled_service_group mssg on mssg.messages_scheduled_service_group_id = mss.group_id
     WHERE mt.name = \'Adherence report weekly\'
         AND mpt.patient_id = :patientId
         AND mpt.actor_id = :actorId
         AND mssg.patient_id = :patientId
         AND mssg.msg_send_time >= :startDateTime
         AND mssg.msg_send_time <= :endDateTime
     ORDER BY 1 DESC;'
WHERE name = 'Adherence report weekly';
