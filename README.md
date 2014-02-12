jayjax
======

The easiest way to call your Java code from JavaScript or form submits. 

**Features**
- Minimal configuration.
- Expose your methods for API calls: i.e. GET /user/profile/123414
- Generates JavaScript for making asynchronous calls to defined methods.
- Automatic serialization of objects, no configuration or coding required.
- Single & Multiple File uploading.

## Example

#### Configuration (WEB-INF/jayjax.xml)
```xml
<controller class="com.company.MyController" name="MyController" javascript="true">
  <function action="/user/(\d+)" method="GET" invoke="getUser(#1)" />
  <function action="/blog/post" method="POST|GET" invoke="post($session[user],title,description)" />
  <function action="/user/profile/update" method="POST" invoke="update($session[user],name,picture)" />
</controller>
```
#### Java
```java
public class MyController {
    public User getUser(long userId) {
        // ...
    }
    public long post(User user, String title, String description) {
        // ...
    }
    public boolean update(User user, String name, Part picture) {
        // ...
    }
}
```
#### JavaScript
```html
<script src="/js/MyController"></script>
<script>
  MyController.getUser(332, {
    success: function(user,status,xhr) {
      // process user
    }
  });
  MyController.post('Title', 'Description', {
    success: function(postId,status,xhr) {
      // success!
    }
  });
</script>
```
#### HTML
```html
<!-- MyController.post -->
<form action="/jj/blog/post" method="POST">
    <input type="text" name="title">
    <textarea name="description"></textarea>
    <button type="submit">Submit Blog Post</button>
</form>
<!-- MyController.update -->
<form action="/jj/user/profile/update" method="POST" enctype="multipart/form-data">
    <input type="text" name="name">
    <input type="file" name="picture">
    <button type="submit">Update Name and Picture</button>
</form>
```
#### Configuration (WEB-INF/web.xml)
```xml
<servlet>
    <servlet-name>jayjax</servlet-name>
    <servlet-class>org.magnos.jayjax.FunctionServlet</servlet-class>
    <multipart-config>
        <max-file-size>52428800</max-file-size>
        <max-request-size>52428800</max-request-size>
        <file-size-threshold>0</file-size-threshold>
    </multipart-config>
</servlet>
<servlet-mapping>
    <servlet-name>jayjax</servlet-name>
    <url-pattern>/jj/*</url-pattern>
</servlet-mapping>
<servlet>
    <servlet-name>jayjax javascript</servlet-name>
    <servlet-class>org.magnos.jayjax.JavascriptServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>jayjax javascript</servlet-name>
    <url-pattern>/js/*</url-pattern>
</servlet-mapping>
```
