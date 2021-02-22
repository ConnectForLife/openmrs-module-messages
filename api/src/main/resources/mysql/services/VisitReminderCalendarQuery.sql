UPDATE messages_template SET calendar_service_query =
    'SELECT EXECUTION_DATE,
        MESSAGE_ID,
        CHANNEL_ID,
        null AS STATUS_ID
    FROM
    (
        SELECT TIMESTAMP(selected_date, :bestContactTime ) AS EXECUTION_DATE,
         1 AS MESSAGE_ID,
         :Service_type AS CHANNEL_ID
        FROM
        DATES_LIST
            JOIN (
                SELECT
                    v.date_started AS visit_dates
                FROM
                    visit v
                    LEFT JOIN (
                        SELECT
                            visit_id,
                            value_reference as visit_status
                        FROM
                            visit_attribute va
                            JOIN visit_attribute_type vat ON va.attribute_type_id = vat.visit_attribute_type_id
                        WHERE
                            vat.name = \'Visit Status\'
                            AND va.voided = 0
                    ) AS visit_statuses ON v.visit_id = visit_statuses.visit_id
                WHERE
                    v.patient_id = :patientId
                    AND v.voided = 0
                    AND visit_statuses.visit_status = \'SCHEDULED\'
            ) dates_of_visit
        WHERE concat(\',\',(
            SELECT property_value
            FROM global_property
            WHERE property =\'message.daysToCallBeforeVisit.default\'), \',\')
                LIKE concat(\'%,\',datediff(visit_dates, selected_date),\',%\')
                AND date(visit_dates) !=  selected_date
    ) dates_before_visit
    WHERE EXECUTION_DATE <= :endDateTime
        AND EXECUTION_DATE >= :startDateTime
        AND EXECUTION_DATE > GET_PREDICTION_START_DATE_FOR_VISIT(:patientId, :actorId, :executionStartDateTime)
        AND CHANNEL_ID != \'Deactivate service\'
    UNION
        SELECT mssg.msg_send_time AS EXECUTION_DATE,
            1 AS MESSAGE_ID,
            mss.channel_type AS CHANNEL_ID,
            mss.status AS STATUS_ID
        FROM messages_scheduled_service mss
            JOIN messages_patient_template mpt ON mpt.messages_patient_template_id = mss.patient_template_id
            JOIN messages_template mt ON mt.messages_template_id = mpt.template_id
            JOIN messages_scheduled_service_group mssg ON mssg.messages_scheduled_service_group_id = mss.group_id
        WHERE mt.name = \'Visit reminder\'
            AND mpt.patient_id = :patientId
            AND mpt.actor_id = :actorId
            AND mssg.patient_id = :patientId
            AND mssg.msg_send_time >= :startDateTime
            AND mssg.msg_send_time <= :endDateTime
        ORDER BY 1 desc;'
WHERE name = 'Visit reminder';
