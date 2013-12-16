



var entities = [];
var queryStatus = {'containsWhere':false};
var editor = CodeMirror.fromTextArea(document.getElementById("sqlQuery"), {
	mode: "text/x-mariadb",
	lineNumbers: true
});
$(function() {
	$("#placeholder").html("");
	
	var fieldsIndex = 0;
	
	$(".wizard-step a").click(function(e) {
		var link = $(this);
		var nextstepId = link.data("nextstep");
		$(this).parent().parent().parent().hide();
		$("#"+nextstepId).fadeIn(600);
		return false;
	});
	
	$("#commit-query").click(function(e) {
		var fields = "SELECT commit.commitId, commit.date";
		editor.setValue(fields + " FROM Commit commit");
		fieldsIndex = fields.length;
		return false;
	});
	
	$(".step2.commit-query").change(function(e){
		var checkbox = $(this);
		checkbox.attr("disabled", "true");
		var query = editor.getValue();
		var fields = query.substring(0, fieldsIndex);
		var joins = query.substring(fieldsIndex, query.lenght);
		fields += ", " + checkbox.data("columns");
		fieldsIndex = fields.length;
		
		var joincolumn = checkbox.data("joincolumn");
		var extrajoin = checkbox.data("extrajoin");
		var commitjoincolumn = checkbox.data("commitjoincolumn");
		var entity = checkbox.data("entity");
		var alias = checkbox.data("alias");
		
		var join = " \nJOIN " + entity + " " + alias + " ON commit." + joincolumn + "=" + alias + "." + commitjoincolumn;
		if (extrajoin) {
			join += " \n" + extrajoin;
		}
		
		query = fields + joins + join;
		editor.setValue(query);
	});
	
	$(".step2.metric-query").click(function(e) {
		e.preventDefault();
		var selected = $(this);
		var entity = selected.data("entity");
		var columns = selected.data("columns") + ", artifact.name";
		var alias = selected.data("alias");
		var fields = "SELECT " + columns;
		var query = fields + " FROM " + entity + " AS " + alias;
		fieldsIndex = fields.length;
		query += "\nJOIN SourceCode sourcecode ON sourcecode.id=" + alias + ".sourceCode_id";
		query += "\nJOIN Modification modification ON modification.id=sourcecode.modification_id";
		query += "\nJOIN Artifact artifact ON artifact.id=modification.artifact_id";
		editor.setValue(query);
		
		var parentdiv = $("#step2-metric");
		var nextstepId = parentdiv.data("nextstep");
		parentdiv.hide();
		$("#"+nextstepId).fadeIn(600);
	});
	
	$(".step3.metric-query").change(function(e) {
		var selected = $(this);
		selected.attr("disabled", "true");
		
		var query = editor.getValue();
		var fields = query.substring(0, fieldsIndex);
		var joins = query.substring(fieldsIndex, query.length);
		
		fields += ", " + selected.data("columns");
		var metricjoincolumn = selected.data("metricjoincolumn");
		var joincolumn = selected.data("joincolumn");
		var entity = selected.data("entity");
		var alias = selected.data("alias");
		
		joins += "\nJOIN " + entity + " AS " + alias + " ON " + joincolumn + "=" + metricjoincolumn; 
		
		query = fields + joins;
		editor.setValue(query);
	});
	
	$(".next-step").parent().submit(function(e) {
		e.preventDefault();
		var step = $(this).parent();
		var where = $('.where-clauses ul');
		where.html("");
		console.log(entities);
		entities.forEach(function(e) {
			metadata[e.entity];
			where.append("<li><a class='where-entity' data-alias="+e.alias+" data-entity='"+e.entity+"' href='#'>"+e.entity+"</a></li>");
		});
		step.hide();
		$(".where-clauses").fadeIn(600);
		$(".where-entity").click(function(e) {
			e.preventDefault();
			var link = $(this);
			var entity = link.data("entity");
			var alias = link.data("alias");
			console.log(entity);
			$(".fields-selection p").text("From which fields from " + entity + " do you want to filter?");
			var fieldList = $(".fields-selection ul");
			fieldList.html("");
			var fields = metadata[entity];
			fields.forEach(function(f){
				fieldList.append("<li><a href='#' class='field-to-select' data-type='"+f.type+"' data-name='"+f.columnName+"'>"+f.columnName +" ("+f.type+")</a></li>");
			}); 
			$(".field-to-select").click(function(e) {
				e.preventDefault();
				var link = $(this);
				$("#condition-form").unbind();
				$("#condition-form").submit(function(e) {
					var query = editor.getValue();
					var restriction = $("#restriction").val();
					if (!queryStatus.containsWhere) {
						query = query + "\nWHERE \n";
						queryStatus.containsWhere = true;
					} else {
						query = query + "\nAND ";
					}
					query = query + alias + "." + restriction; 
					editor.setValue(query);
					$(".condition").hide();
					$(".fields-selection").fadeIn(600);
					e.preventDefault();
				});
				$("#restriction").val(link.data("name"));
				$(".condition p.desc").text("Define the restriction for the column "+link.data("name")+" of type " + link.data("type") + ":");
				$(".fields-selection").hide();
				$(".condition").fadeIn(600);
			});
			$(".where-clauses").hide();
			$(".fields-selection").fadeIn(600);
			
		});
	});
	$("#fields-selection-submit").click(function(e) {
		e.preventDefault();
		$(".fields-selection").hide();
		$(".where-clauses").fadeIn(600);
	});
	
	$(".entity-selection").click(function() {
		putEntity(this);
	});
	
	$(".entity-selection").change(function() {
		putEntity(this);
	});
	
	$("#finish").click(function(e) {
		e.preventDefault();
        $('html, body').animate({
            scrollTop: $("#generated-query").offset().top
        }, 2000);
	});
	
	function putEntity(self) {
		var selected = $(self);
		var entity = {'alias': selected.data("alias"), 'entity': selected.data("entity")};
		entities.push(entity);
	}
});
