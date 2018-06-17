<html>
<head>
  <title>Thread Status</title>
  <style>
    <!-- 
    td.running {
        background-color:lime;
    }
    td.monitor {
        background-color:lightsalmon;
    }
    td.sleeping {
      background-color:slateblue;
    }
    td.park {
        background-color:orange;
    }
    td.wait {
        background-color:yellow;
    }
    td.zombie {
        background-color:gray;
    }
    td.unknown {
        background-color:black;
        color: white;
    }
    textarea.thread_detail {
        display: none;
    }
    -->
  </style>
  <script type="text/javascript" defer>
    (function(){
      document.addEventListener("DOMContentLoaded", function(){
      Array.prototype.slice.call(document.querySelectorAll("td.thread")).
        forEach(function(t){
          t.addEventListener("click", function(){
            console.log(t);
            var detail = t.querySelector(".thread_detail");
            detail.style.width = "1024px";
            detail.style.height = "600px";
            var win = window.open();
            win.document.write("<html><head></head><body></body></html>");
            win.document.body.innerHTML = detail.outerHTML;
        })})
      });
    })();
  </script>

</head>
<body>
  <table>
    <colgroup>
      <col style="background-color: white">
    </colgroup>
    <th>thread name</th>
    <#list timestamps as ts>
      <th>${ts?datetime?html}</th>
    </#list>
    <#list tIds as tId>
      <tr>
        <td>${nameMap.get(tId)?html}</td>
        <#assign entry_groupby_id = entries.get(tId)/>
        <#list timestamps as ts>
          <#if (entries.get(tId).get(ts))??>
               <td class="thread ${entries.get(tId).get(ts).getState()?html?lower_case}">
                 <div>${entries.get(tId).get(ts).getState()?html?lower_case}<div>
                 <textarea readonly class="thread_detail">
<#list entries.get(tId).get(ts).getLines() as line>
${line?html}
</#list>
                 </textarea>
               </td>
            <#else>
            <td>not found</td>
          </#if>
        </#list>
      </tr>
    </#list>
  </table>
</body>
</html>