UPDATE messages_template SET service_query =
    'SELECT EXECUTION_DATE,
        MESSAGE_ID,
        null AS STATUS_ID,
        visitTypeId,
        locationId,
        dateStarted,
        visitId,
        timeStarted,
        PATIENT_ID,
        ACTOR_ID
    FROM
    (
        SELECT selected_date AS EXECUTION_DATE,
         1 AS MESSAGE_ID,
         visitTypeId,
         locationId,
         dateStarted,
         visitId,
         visitTime AS timeStarted,
         PATIENT_ID,
         ACTOR_ID
        FROM
        DATES_LIST_10_DAYS

            JOIN (
                SELECT
                    v.date_started AS visit_dates,
                    v.visit_type_id AS visitTypeId,
                    v.location_id AS locationId,
                    v.visit_id AS visitId,
                    v.date_started AS dateStarted,
                    visit_times.visit_time AS visitTime,
                    v.patient_id AS PATIENT_ID,
                    null AS ACTOR_ID
                FROM
                    visit v
                    LEFT JOIN (
                        SELECT
                            visit_id,
                            value_reference as visit_time
                        FROM
                            visit_attribute va
                            JOIN visit_attribute_type vat ON va.attribute_type_id = vat.visit_attribute_type_id
                        WHERE
                            vat.name = \'Visit Time\'
                    ) AS visit_times ON v.visit_id = visit_times.visit_id
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
                    v.voided = 0
                    AND visit_statuses.visit_status = \'SCHEDULED\'
            ) dates_of_visit
        WHERE concat(\',\',(
            SELECT property_value
            FROM global_property
            WHERE property =\'message.daysToCallBeforeVisit.default\'), \',\')
                LIKE concat(\'%,\',datediff(visit_dates, selected_date),\',%\')
                AND date(visit_dates) !=  selected_date
    ) dates_before_visit
    UNION
        SELECT mssg.msg_send_time AS EXECUTION_DATE,
            1 AS MESSAGE_ID,
            mss.status AS STATUS_ID,
            null AS visitTypeId,
            null AS locationId,
            null AS dateStarted,
            null AS visitId,
            null AS timeStarted,
            mpt.patient_id AS PATIENT_ID,
            mpt.actor_id AS ACTOR_ID
        FROM messages_scheduled_service mss
            JOIN messages_patient_template mpt ON mpt.messages_patient_template_id = mss.patient_template_id
            JOIN messages_template mt ON mt.messages_template_id = mpt.template_id
            JOIN messages_scheduled_service_group mssg ON mssg.messages_scheduled_service_group_id = mss.group_id
        WHERE mt.name = \'Visit reminder\'
            AND mpt.patient_id = mpt.patient_id
            AND mpt.actor_id = mpt.actor_id
            AND mssg.patient_id = mpt.patient_id
            AND mssg.msg_send_time >= :startDateTime
            AND mssg.msg_send_time <= :endDateTime
        ORDER BY 1 desc;'
WHERE name = 'Visit reminder';
