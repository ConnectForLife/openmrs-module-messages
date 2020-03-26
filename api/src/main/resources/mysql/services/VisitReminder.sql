UPDATE messages_template SET service_query =
    'SELECT EXECUTION_DATE,
        MESSAGE_ID,
        CHANNEL_ID,
        null AS STATUS_ID,
        visitTypeId,
        locationId,
        dateStarted,
        visitId,
        timeStarted

    FROM
    (
        SELECT TIMESTAMP(selected_date, :bestContactTime ) AS EXECUTION_DATE,
         1 AS MESSAGE_ID,
         :Service_type AS CHANNEL_ID,
         visitTypeId,
         locationId,
         dateStarted,
         visitId,
         valueReference AS timeStarted
        FROM
        DATES_LIST

            JOIN (
                SELECT v.date_started AS visit_dates,
                    v.visit_type_id AS visitTypeId,
                    v.location_id AS locationId,
                    v.visit_id AS visitId,
                    v.date_started AS dateStarted,
                    va.value_reference AS valueReference
                FROM visit v
                JOIN visit_attribute va ON va.visit_id=v.visit_id
                JOIN visit_attribute_type vat ON va.attribute_type_id = vat.visit_attribute_type_id
                WHERE v.patient_id = :patientId AND vat.name=\'Visit Time\' AND v.voided = 0
        ) dates_of_visit
        WHERE concat(\',\',(
            SELECT property_value
            FROM global_property
            WHERE property =\'message.daysToCallBeforeVisit.default\'), \',\')
                LIKE concat(\'%,\',datediff(visit_dates, selected_date),\',%\')
                AND date(visit_dates) !=  selected_date
    )dates_before_visit
    WHERE EXECUTION_DATE <= :endDateTime
        AND EXECUTION_DATE >= :startDateTime
        AND EXECUTION_DATE > GET_PREDICTION_START_DATE_FOR_VISIT(:patientId, :actorId)
        AND CHANNEL_ID != \'Deactivate service\'
    UNION
        SELECT mssg.msg_send_time AS EXECUTION_DATE,
            1 AS MESSAGE_ID,
            mss.channel_type AS CHANNEL_ID,
            mss.status AS STATUS_ID,
            null AS visitTypeId,
            null AS locationId,
            null AS dateStarted,
            null AS visitId,
            null AS timeStarted
        FROM messages_scheduled_service mss
            JOIN messages_patient_template mpt ON mpt.messages_patient_template_id = mss.patient_template_id
            JOIN messages_template mt ON mt.messages_template_id = mpt.template_id
            JOIN messages_scheduled_service_group mssg ON mssg.messages_scheduled_service_group_id = mss.group_id
        WHERE mt.name = \'Visit reminder\'
            AND mpt.patient_id = :patientId
            AND mpt.actor_id = :actorId
            AND mssg.msg_send_time >= :startDateTime
            AND mssg.msg_send_time <= :endDateTime
        ORDER BY 1 desc;'
WHERE name = 'Visit reminder';
