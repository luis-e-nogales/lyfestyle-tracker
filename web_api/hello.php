<?php
    $con = oci_connect("ora_XXX", "aXXXXXX", "XXXXXX");
    if ($con) {
		//echo "Successfully connected to Oracle.<br>\n";
	} else {
		$err = oci_error();
		echo "Oracle Connect Error " . $err['message'] . "<br>\n";
    }

    $query_type = $_POST['query_type'];
    $columns = $_POST['columns'];
    $table = $_POST['table'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $extra = $_POST['extra']; // USed to put in whole sql query if query_type == "Special"

    $s;
    if ($query_type == 'select' && $columns == 'all') {
        $s = oci_parse($con, 'SELECT * FROM ' . $table);
    } else if ($query_type == 'insert') {
        $s = oci_parse($con, 'INSERT INTO ' . $table . ' VALUES ' . '<todo - insert values>');
    } else if ($query_type == 'describe') {
        $s = oci_parse($con, 'SELECT * FROM ' . $table);
        oci_execute($s, OCI_DESCRIBE_ONLY);
        $ncols = oci_num_fields($s);
        $resultArray = array();
        for ($i = 1; $i <= $ncols; $i++) {
            $tempArray = [
                oci_field_name($s, $i) => oci_field_type($s, $i),
            ];
            array_push($resultArray, $tempArray);
        }
        echo json_encode($resultArray);
        oci_free_statement($s);
        oci_close($con);
        exit();
    } else if ($query_type == 'authenticate') {
        $s = oci_parse($con, 'SELECT * FROM People WHERE username = \'' . $username . '\'');
        oci_execute($s, OCI_DEFAULT);
        $row = oci_fetch_row($s);

        if ($row == "") {
            echo ('[{"result":"user does not exist"}]');
        } else {
            if ($row[2] == $password) {
              echo ('[{"result":"sucess"}]');
            } else {
              echo ('[{"result":"incorrect password"}]');
            }
        }

        oci_free_statement($s);
        oci_close($con);
        exit();
    } else if ($query_type == 'special') {
      $s = oci_parse($con, $extra);
    } else if ($query_type == 'special_change') {
      $s = oci_parse($con, $extra);
      oci_execute($s, OCI_DEFAULT);
      echo ('[{"result":"inserted"}]');
      oci_commit($con);
      oci_free_statement($s);
      oci_close($con);
      exit();
    }


    oci_execute($s, OCI_DEFAULT);

    $resultArray = array();
    $tempArray = array();

    while ($row = oci_fetch_object($s)) {
        $tempArray = $row;
        array_push($resultArray, $tempArray);
        //echo $row->TABLE_NAME . "<br>\n";
    }

    echo json_encode($resultArray);

    oci_commit($con);
    oci_free_statement($s);
    oci_close($con);
?>
