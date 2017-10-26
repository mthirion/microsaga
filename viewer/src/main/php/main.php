
<?php

print '<html>
    <head>
        <title>PHP-MySQL Viewer</title>
       
<style type="text/css">
table.gridtable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}
table.gridtable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}
</style>

    </head>
    <body>
    			<div id="dv1">
    			<h3>View</h3>
    			<table class="table, gridtable"">
				<thead>
					<tr>
						<th align="center">Nom</th>
						<th align="center">Prenom</th>
						<th align="center">Numero de compte</th>
						<th align="center">Numero de carte</th>
					</tr>
				</thead>
';

				print "<tbody>";

					$con = mysql_connect('localhost', 'root', 'mysql');
					mysql_select_db('fis2demo', $con);


					$qry_em = 'select * from MView';
					$qry_res = mysql_query($qry_em);

					while($row = mysql_fetch_assoc($qry_res)) {
					print "<tr>";
						print "<td>";
							echo "{$row['nom']}";
						print ""</td>;

						print "<td>";
			 				echo "{$row['prenom']}";
						print "</td>";
					
						print "<td>";
		 					echo " {$row['accountNumber']}";
						print "</td>";
					
						print "<td>";
				 			echo "{$row['cardNumber']}";
						print "</td>";
					print "</tr>";
					}
	

			print "</tbody>";

?>
