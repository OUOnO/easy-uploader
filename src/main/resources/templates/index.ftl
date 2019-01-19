<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Uploader</title>
  <link rel="stylesheet" href="bootstrap/css/bootstrap.css">
  <link rel="stylesheet" href="bootstrap/css/fileinput.css">
  <link rel="stylesheet" href="bootstrap/css/bootstrap-table.min.css">
</head>
<body>
<div class="container">
  <div class="row clearfix">
    <div class="col-md-2 column"></div>
    <div class="col-md-8 column" style="margin-top: 50px">
      <div class="panel panel-default">
        <div class="panel-heading">
          <div class="row">
            <div class="col-md-2 column">
              <h3 class="panel-title">
                File upload
              </h3>
            </div>
            <div class="col-md-8 column"></div>
            <#--<div class="col-md-2 column">-->
              <#--<button type="button" class="btn btn-primary" onclick="showList();">Show list</button>-->
            <#--</div>-->
          </div>

        </div>
        <div class="panel-body">
          <input id="f_upload" type="file" class="file-loading"/>
          <div id="tableContainer" style="margin-top: 50px">
            <table
              id="table"
              data-toggle="table"
              data-url="/list">
              <thead>
              <tr>
                <th data-field="fileName">File name</th>
                <th data-field="downloadUrl" data-formatter="urlFormatter">Download url</th>
                <th data-field="code">Code</th>
              </tr>
              </thead>
            </table>
          </div>
        </div>
      </div>
    </div>
    <div class="col-md-2 column"></div>
  </div>
</div>


<div class="modal fade" id="upload_res" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
                aria-hidden="true">×
        </button>
        <h4 class="modal-title" id="myModalLabel">
          Upload Success!
        </h4>
      </div>
      <div class="modal-body">
        Download url：<a href="" id="download-url" target="_blank"></a><br>
        Code：<span id="download-code"></span>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default"
                data-dismiss="modal">Close
        </button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

</body>
<script src="jquery/jquery.js"></script>
<script src="bootstrap/js/bootstrap.js"></script>
<script src="bootstrap/js/fileinput.js"></script>
<script src="bootstrap/js/bootstrap-table.min.js"></script>

<script>
  $(document).ready(function () {

    initUploader();

    $('#upload_res').on('hide.bs.modal', function () {
      window.location.reload();
    })

  });

  function initUploader() {

    var $f_upload = $('#f_upload');
    $f_upload.fileinput({
      uploadUrl: '/upload',
      enctype: 'multipart/form-data',
      showUpload: true,
      allowedPreviewTypes: ['image', 'html', 'text'],
      dropZoneEnabled: false,
      slugCallback: function (filename) {
        return filename.replace('(', '_').replace(']', '_');
      },
    });

    $f_upload.on("fileuploaded", function (event, data, previewId, index) {
      var res = data.response;
      if (res.code === 200) {
        $('#download-code').text(res.download_code);
        var $du = $('#download-url');
        $du.text(res.download_url);
        $du.attr('href', res.download_url);
        $('#upload_res').modal('show');
      }
    });

    $f_upload.on('fileerror', function (event, data, msg) {
      console.log('error');
    });

  }

  function urlFormatter(value, row) {
    return '<a href="' + value + '" target="_blank">' + value + '</a>';
  }

</script>
</html>