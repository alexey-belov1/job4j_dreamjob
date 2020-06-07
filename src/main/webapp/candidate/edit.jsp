<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.4.1.min.js" ></script>

    <script>
        $(document).ready(function() {
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080/dreamjob/city',
                dataType: 'json'
            }).done(function(data) {
                let select = $('select[name="city"]');
                data.forEach(el => select.append('<option value=' + el.id + '>' + el.name + '</option>'));
            }).fail(function(err){
                alert(err);
            });
        });

        function validate() {
            let name = $('input[name="name"]');
            if (name.val() == '') {
                let msg = $('#msg');
                msg.text('Заполните поле: ' + name.attr('title'));
                msg.removeAttr('hidden');
                return false;
            }
            return true;
        }
    </script>

    <title>Работа мечты</title>
</head>
<body>
<%
    String id = request.getParameter("id");
    Candidate candidate = new Candidate(0, "", 0, 0);
    if (id != null) {
        candidate = PsqlStore.instOf().findByIdCandidate(Integer.valueOf(id));
    }
%>
<div class="container pt-3">
    <div class="container">
        <div class="row">
            <ul class="nav">
                <li class="nav-item">
                    <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp"> <c:out value="${user.name}"/> | Выйти</a>
                </li>
            </ul>
        </div>
    </div>
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новый кандидат.
                <% } else { %>
                Редактирование кандидата.
                <% } %>
            </div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/candidates.do?id=<%=candidate.getId()%>" method="post">
                    <div class="form-group">
                        <label>Имя</label>
                        <input type="text" class="form-control" name="name" value="<%=candidate.getName()%>" title="имя">
                        <label>Город</label>
                        <select class="form-control" name="city"></select>
                    </div>
                    <button type="submit" class="btn btn-primary" onclick="return validate();">Сохранить</button>
                    <p id="msg" hidden></p>
                </form>
            </div>
            <% if (id != null) { %>
            <div class="card-header">Фото</div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/upload?id=<%=candidate.getId()%>" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <input type="file" name="file">
                    </div>
                    <button type="submit" class="btn btn-primary">Сохранить</button>
                </form>
            </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>