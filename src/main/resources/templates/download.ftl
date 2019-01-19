<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Download</title>
  <link rel="stylesheet" href="bootstrap/css/bootstrap.css">
  <link rel="stylesheet" href="bootstrap/css/fileinput.css">
</head>
<body>
<div class="container">
  <div class="row clearfix">
    <div class="col-md-4 column"></div>
    <div class="col-md-4 column" style="margin-top: 50px">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">
            File Download
          </h3>
        </div>
        <div class="panel-body">
          <div class="input-group">
            <span class="input-group-addon">code</span>
            <input id="download-code" type="text" class="form-control" placeholder="please input the code">
          </div>
        </div>
        <div class="panel-footer">
          <div class="row">
            <div class="col-md-8 column"></div>
            <div class="col-md-4 column">
              <button type="button" class="btn btn-primary" onclick="download();">Download</button>
            </div>
          </div>

        </div>
      </div>
    </div>
    <div class="col-md-4 column"></div>
  </div>
</div>
<input type="hidden" id="download-url" value="${downloadUrl}">
</body>
<script src="jquery/jquery.js"></script>
<script src="bootstrap/js/bootstrap.js"></script>
<script>
function download() {
  var code = $('#download-code').val();
  var url = $('#download-url').val();
  if(!code) {
    alert('Please input the code.');
    return;
  }

  window.open('/download/' + encodeURIComponent(url) + '/' + encodeURIComponent(code));
}

</script>
</html>