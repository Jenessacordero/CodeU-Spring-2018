// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.store.basic.UploadedImagesStore;
import codeu.model.data.Images;
import java.util.List;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the test image uploading page. */
public class ImageUploadTestServlet extends HttpServlet {

    /** Store class that gives access to Images */
    private UploadedImagesStore imageStore;


  /**
   * Set up state for handling conversation-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setImageStore(UploadedImagesStore.getInstance());
  }


  /**
   * Sets the ImageStore used by this servlet. This function provides a common setup method for use by the test
   * framework or the servlet's init function.
   */
  void setImageStore(UploadedImagesStore imageStore) { this.imageStore = imageStore; }


  /**
   * This function fires when a user navigates to the conversations page. It gets all of the
   * conversations from the model and forwards to conversations.jsp for rendering the list.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    List<Images> images = imageStore.returnAllImages();
    request.setAttribute("images", images);
    request.getRequestDispatcher("/WEB-INF/view/imageuploadtest.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the image test page page.
   * retrieves filename from the given form then creates a new Image Object that is stored in the
   * Image data store */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    // for test purposes, only redirects to page

      String filename = request.getParameter("image");
      Images uploadImage = new Images(filename, UUID.randomUUID());
      imageStore.addImage(uploadImage);
    response.sendRedirect("/imageuploadtest");
  }
}
