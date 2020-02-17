UPDATE messages_template SET service_query =
    'SELECT EXECUTION_DATE,
        MESSAGE_ID,
        CHANNEL_ID,
        NULL AS STATUS_ID,
        HEALTH_TIP_ID
    FROM (
        SELECT TIMESTAMP(selected_date, times) AS EXECUTION_DATE,
            1 AS MESSAGE_ID,
            :Service_type AS CHANNEL_ID
        FROM (
            SELECT *
            FROM
                (SELECT * FROM DATES_LIST) dates_list
            WHERE selected_date >= DATE(:startDateTime)
                AND selected_date <= DATE(:endDateTime)
                AND (DAYOFMONTH(selected_date) < IF
                    (
                        :Frequency_of_the_message = \'Weekly\'
                        OR :Frequency_of_the_message = \'Daily\', 32, 8
                    )
                )
                AND :Week_day_of_delivering_message
                     LIKE concat(\'%\',DAYNAME(selected_date),\'%\')
        ) dates
            JOIN (SELECT :bestContactTime times) timeslist) temp
            JOIN (
                SELECT group_concat(HT_ID) AS HEALTH_TIP_ID
                FROM (
                    (
                        SELECT @row_number\\:=@row_number + 1, concept_id AS HT_ID
                        FROM concept_set AS cs, (select @row_number\\:=0) as rn
                        WHERE
                            cs.concept_set IN (
                                SELECT concept_id
                                FROM concept_name cn
                                WHERE :Categories_of_the_message LIKE concat(\'%\', cn.name, \'%\')
                            )
                            AND concept_id not IN (
                                SELECT question
                                FROM (SELECT question, source_id, answered_time FROM messages_actor_response mar WHERE source_type like \'SCHEDULED_SERVICE_GROUP\') mar
                                    JOIN messages_scheduled_service_group mssg on mar.source_id = mssg.messages_scheduled_service_group_id
                                    JOIN messages_scheduled_service mss on mssg.messages_scheduled_service_group_id = mss.group_id
                                    JOIN messages_patient_template mpt on mss.patient_template_id = mpt.messages_patient_template_id
                                    JOIN messages_template mt on mpt.template_id = mt.messages_template_id
                                    WHERE mt.name = \'Health tip\'
                                        AND mpt.patient_id = :patientId
                                        AND mss.status = \'DELIVERED\'
                                    AND mar.answered_time >
                                        IF( (SELECT COUNT(*) FROM
                                        (SELECT question, source_id, answered_time FROM messages_actor_response mar WHERE source_type like \'SCHEDULED_SERVICE_GROUP\') mar
                                        JOIN messages_scheduled_service_group mssg on mar.source_id = mssg.messages_scheduled_service_group_id
                                        JOIN messages_scheduled_service mss on mssg.messages_scheduled_service_group_id = mss.group_id
                                        JOIN messages_patient_template mpt on mss.patient_template_id = mpt.messages_patient_template_id
                                        JOIN messages_template mt on mpt.template_id = mt.messages_template_id
                                        WHERE mt.name = \'Health tip\'
                                            AND mpt.patient_id = :patientId
                                            AND mss.status = \'DELIVERED\') <
                                        (SELECT COUNT(*) FROM concept_set cs
                                        WHERE cs.concept_set IN
                                            (SELECT concept_id FROM concept_name cn
                                            WHERE :Categories_of_the_message
                                            LIKE concat(\'%\', cn.name, \'%\'))),
                                          \'1900-01-01\',
                                          IFNULL(
                                            (SELECT MAX(answered_time) FROM
                                            (SELECT question, source_id, answered_time FROM messages_actor_response mar WHERE source_type like \'SCHEDULED_SERVICE_GROUP\') mar
                                            JOIN messages_scheduled_service_group mssg on mar.source_id = mssg.messages_scheduled_service_group_id
                                            JOIN messages_scheduled_service mss on mssg.messages_scheduled_service_group_id = mss.group_id
                                            JOIN messages_patient_template mpt on mss.patient_template_id = mpt.messages_patient_template_id
                                            JOIN messages_template mt on mpt.template_id = mt.messages_template_id
                                            WHERE mt.name = \'Health tip\'
                                                AND mpt.patient_id = :patientId
                                                AND mss.status = \'DELIVERED\'
                                                AND mar.question = (
                                            (SELECT concept_id FROM concept_set AS cs WHERE cs.concept_set IN
                                            (SELECT concept_id FROM concept_name cn WHERE :Categories_of_the_message
                                            LIKE concat(\'%\', cn.name, \'%\'))
                                            ORDER BY cs.sort_weight DESC
                                            limit 1))),
                                          \'1900-01-01\')
                                        )
                            )
                        AND @row_number <
                        (
                            SELECT property_value
                            FROM global_property
                            WHERE property = \'messages.numberOfHealthTipsPlayedPerOneCall\'
                        )
                        ORDER BY cs.sort_weight ASC
                    ) tips
                )
            ) gc
    WHERE EXECUTION_DATE > GET_PREDICTION_START_DATE_FOR_HEALTH_TIP(:patientId, :actorId)
        AND EXECUTION_DATE >= :startDateTime
        AND EXECUTION_DATE <= :endDateTime
        AND CHANNEL_ID != \'Deactivate service\'
UNION
    SELECT mssg.msg_send_time AS EXECUTION_DATE,
        1 AS MESSAGE_ID,
        mss.channel_type AS CHANNEL_ID,
        mss.status AS STATUS_ID,
        null AS HEALTH_TIP_ID
    FROM messages_scheduled_service mss
        JOIN messages_patient_template mpt on mpt.messages_patient_template_id = mss.patient_template_id
        JOIN messages_template mt on mt.messages_template_id = mpt.template_id
        JOIN messages_scheduled_service_group mssg on mssg.messages_scheduled_service_group_id = mss.group_id
    WHERE mt.name = \'Health tip\'
        AND mpt.patient_id = :patientId
        AND mpt.actor_id = :actorId
        AND mssg.msg_send_time >= :startDateTime
        AND mssg.msg_send_time <= :endDateTime
    ORDER BY 1 DESC ;'
WHERE name = 'Health tip';
