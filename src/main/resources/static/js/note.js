document.getElementById("openModalButton").addEventListener("click", function () {
    $("#myModal").modal("show");
    $(".updateNote").hide();
    $(".add").show();
});
document.querySelector(".notify").addEventListener("click", function () {
    var notifyContent = document.querySelector(".notify-content");
    notifyContent.classList.toggle("active"); // Thêm hoặc xóa lớp "active"
});


var stompClient = null;
function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/notifications', function (message) {
            // var notification = JSON.parse(message.body);
            showNotification(message.body);
        });
    });
}


function checkNotifications() {
    $.ajax({
        url: "/savenotification",
        type: "get",

    }).done(function (data) {
        $.ajax({
            url: "/getnotify",
            type: "get",
            dataType: "json"
        }).done(function (data) {
            connect();
            if (data.count > 0) {
                $(".badge").text(data.count);}

        });
    });

}
setInterval(checkNotifications, 60000);

function showNotification(notification) {
    if (Notification.permission !== "granted") {
        Notification.requestPermission().then(function (permission) {
            if (permission === "granted") {
                var notification = new Notification("Thông báo của NoteWebsite", {
                    body: notification,
                    icon: "link đến biểu tượng (icon) của thông báo",
                });

                notification.onclick = function () {
                    window.location.href = "http://localhost:8080/home";
                };
            }
        });
    } else {
        var notification = new Notification("Thông báo của NoteWebsite", {
            body: notification,
            icon: "link đến biểu tượng (icon) của thông báo",
        });

        notification.onclick = function () {

            window.location.href = "http://localhost:8080/home";
        };
    }
}


$(document).ready(function (){

    checkNotifications();
    $(document).on("click",".add",function () {

        var htmlContent = quill.root.innerHTML;
        var selectoption = $("#tag option:selected");
        var tagid = selectoption.data("id");
        var tagname = selectoption.data("name");

        var formdata = {

            tag:{id:tagid,name:tagname} ,
            title: $("#title").val(),
            end_at: $("#end_at").val(),
            content:htmlContent
        }

        $.ajax({
            url:"/add",
            type:"POST",
            contentType: "application/json",
            data: JSON.stringify(formdata)


        }).done(function (data) {
            $(".listnote").load(window.location + " .listnote");
            $("#myModal").modal("hide");
        });
    });
    $(document).on("click",".deletenote",function () {
        if (confirm("ban muon xoa chu ?")){
            var id = $(".deletenote").data("id");
            $.ajax({
                url: "/delete/"+id,
                type: "delete"
            }).done(function (data) {
                $(".listnote").load(window.location + " .listnote");

            })
        }
    });
    $(document).on("click",".updatenote",function () {

        var id = $(this).data("id");
        $.ajax({
            url:"/getnote/"+id,
            type:"get"
        }).done(function (data) {
            $("#noteid").val(data.id);
            $(".updateNote").show();
            $(".add").hide();
            $("#title").val(data.title);
            $("#end_at").val(data.end_at);
            $("#tag").val(data.tag.id);

            quill.clipboard.dangerouslyPasteHTML(data.content);
            // $("#editorcontainer").html(data.content);
            $("#myModal").modal("show");
        })
    });
    $(document).on("click",".updateNote",function () {
            var id = $("#noteid").val();
            var htmlContent = quill.root.innerHTML;
            var selectoption = $("#tag option:selected");
            var tagid = selectoption.data("id");
            var tagname = selectoption.data("name");

            var formdata = {
                id:id,
                tag:{id:tagid,name:tagname} ,
                title: $("#title").val(),
                end_at: $("#end_at").val(),
                content:htmlContent
            }
            $.ajax({
                url:"/update",
                type:"put",
                contentType: "application/json",
                data: JSON.stringify(formdata)
            }).done(function (data) {
                $(".listnote").load(window.location + " .listnote");
                $("#myModal").modal("hide");
            })

    });


    document.getElementById("inputsearch").addEventListener("input", function () {

        var keyword = this.value.toLowerCase();


        var notes = document.querySelectorAll(".note");
        notes.forEach(function (note) {
            var title = note.querySelector(".title h6").textContent.toLowerCase();
            if (title.includes(keyword)) {
                note.style.display = "block";
            } else {
                note.style.display = "none";
            }
        });
    });
    $(document).on("click",".note",function () {
        var id = $(this).data("id");
        $.ajax({
            url:"getnote/"+id,
            type:"get",

        }).done(function (data) {
            $(".maincontent").empty();
            var note = '<div class="detailnote">' +
                '<div class="contenttitle">' +
                '<h1>' + data.title + '</h1>' +
                '</div>' +
                '<div class="taglist">' +
                '<div class="tag">' +
                '<div class="tagicon">' +
                '<i class="fa-solid fa-tag"></i>' +
                '</div>' +
                '<div class="tagname">' + data.tag.name + '</div>' +
                '</div>' +
                '</div>' +
                '<div class="content">' +
                data.content +
                '</div>' +
                '</div>';
            $(".maincontent").append(note);
        });
    });


});