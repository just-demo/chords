<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<script type="text/javascript">
	$(document).ready(function() {
		$("#print").click(function() {
			window.print();
		});

		$(".vote").click(function() {
			$.ajax({
				url : $(this).data("url"),
				type: "POST",
				dataType: "json",
				data: {rate: $(this).data("rate")},
				success : function(data) {
					//console.log($(this).data("url"));
					//console.log($(this).data("rate"));
					//console.log(data);
					//alert(data);
					if (data.status == "200") {
						$("#positive_rate").html("+" + data.positiveRate);
						$("#negative_rate").html("-" + data.negativeRate);
						//$(".vote").hide();
						$(".vote").attr("disabled", "disabled");
					}
				},
				error: function(data){
					//alert(data);
				}
			});
		});
	});
</script>
<tiles:insertDefinition name="head.chord.popup" />
