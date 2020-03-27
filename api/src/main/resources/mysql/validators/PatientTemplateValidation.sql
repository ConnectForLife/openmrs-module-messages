CREATE PROCEDURE validate_messages_patient_template(
	IN actor_id INT,
	IN patient_id INT,
	IN template_id INT
)
BEGIN
	IF ((
		SELECT COUNT(*) FROM messages_patient_template pt
		WHERE pt.actor_id = actor_id
			AND pt.patient_id = patient_id
			AND pt.template_id = template_id
			AND pt.voided = false
	) > 1) THEN
		SET @error_message = CONCAT(
			'Duplicate combinantion of actor_id-patient_id-template_id (',
			actor_id, '-', patient_id, '-', template_id, ') ',
		    'for not voided messages_patient_template'); -- it is because CONCAT cannot be invoked in the next line
		SIGNAL SQLSTATE '23000' SET MESSAGE_TEXT = @error_message; -- it rollbacks the current transaction
	END IF;
END;