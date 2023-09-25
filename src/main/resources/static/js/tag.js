$(document).ready(function () {
    $(document).on("click",".save",function () {
        var title = $("#tagname").val();
        var dataToSend = { "name": title };
        $.ajax({
            url:"Tag",
            type:"post",
            contentType:"application/json",
            data:JSON.stringify(dataToSend)
        }).done(function (response) {
            if (response.errors!=null){

            }else {
                $("#tabletag").load(window.location + " #tabletag");
                $("#myModal").modal("hide");
                $("#tag").val("");
            }
        });
    });
    $(document).on("click",".update",function () {
        var id = $(this).data("id");
        $(".save").hide();
        $(".updatetag").show();
        $.ajax({
            url: "Tag/gettag/"+id,
            type: "get",
        }).done(function (response) {

            $("#tagname").val(response.name);
            $("#tagid").val(response.id)
            $("#myModal").modal("show");
        })


        
    });
    $(document).on("click",".updatetag",function () {
        var id = $("#tagid").val();
        var title = $("#tagname").val();
        var dataToSend = {
            "name": title,
            "id":  id
        };
        $.ajax({
            url:"Tag/update",
            type:"put",
            contentType:"application/json",
            data:JSON.stringify(dataToSend)
        }).done(function (response) {
            if (response.errors!=null){

            }else {
                $("#tabletag").load(window.location + " #tabletag");
                $("#myModal").modal("hide");
                $("#tagname").val("");

            }
        });
    })
    $(document).on("click",".delete",function () {
        if (confirm("ban muon xoa ?")){
            var id = $(this).data("id");
            $.ajax({
                url:"Tag/delete/"+id,
                type:"delete",


            }).done(function (response) {
                $("#tabletag").load(window.location + " #tabletag");
            })
        }

    })
})