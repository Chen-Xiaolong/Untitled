$('.form').find('input, textarea').on('keyup blur focus', function (e) {

  var $this = $(this),
      label = $this.prev('label');

	  if (e.type === 'keyup') {
			if ($this.val() === '') {
          label.removeClass('active highlight');
        } else {
          label.addClass('active highlight');
        }
    } else if (e.type === 'blur') {
    	if( $this.val() === '' ) {
    		label.removeClass('active highlight');
			} else {
		    label.removeClass('highlight');
			}
    } else if (e.type === 'focus') {

      if( $this.val() === '' ) {
    		label.removeClass('highlight');
			}
      else if( $this.val() !== '' ) {
		    label.addClass('highlight');
			}
    }

});

$('.tab a').on('click', function (e) {

  e.preventDefault();

  $(this).parent().addClass('active');
  $(this).parent().siblings().removeClass('active');

  target = $(this).attr('href');

  $('.tab-content > div').not(target).hide();

  $(target).fadeIn(600);

});
$('#log').on('click',function(){
    var data2={};
    var x=document.getElementById('user-name').value;
    var y=document.getElementById('user-password').value;
    data2.username=x;
    data2.password=y;
    console.log(data2);
    $.post("http://localhost:8080/Untitled/user/login",
        data2,
        function (data) {
            if (data.status==1) {
                window.location.href="../../../../Untitled/main/AddSkills.html";
                alert("登陆成功");
            }
            else{
                alert("登陆失败");

            }
        })
});
$('#register_log').on('click',function(){
    var data3={};
    var x=document.getElementById('register_name').value;
    var y=document.getElementById('register_password').value;
    data3.username=x;
    data3.password=y;
    console.log("123"+data3);
    $.post("http://localhost:8080/Untitled/admin/login",
        data3,
        function (data) {
            if (data.status==1) {
                window.location.href="../../../Untitled/main/management.html";
                alert("登陆成功");
            }
            else{
                alert("登陆失败");

            }
        })
});
$('#register').on('click',function () {
   var data1={};
   var x=document.getElementById('register-name').value;
   var y1=document.getElementById('register-password').value;
   var y2=document.getElementById('register-password-again').value;
   if(y1==y2){
       data1.username=x;
       data1.password=y1;
       $.post("http://localhost:8080/Untitled/user/register",
           data1,
           function (data) {
               if(data.status==2){
                   alert("注册成功");
               }
               else {
                   alert("注册失败");
               }
           })
   }
   else{
       alert("前后密码不同，请重新输入!");
   }
});

// jQuery('#admin').click(function () {
//    jQuery('.new_form').show();
//    jQuery('.form').hide();
// });
jQuery('#')