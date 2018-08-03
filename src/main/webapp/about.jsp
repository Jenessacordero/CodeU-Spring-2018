<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<!DOCTYPE html>
<html>
<head>
  <title>CodeU Chat App</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

<%@ include file="WEB-INF/view/nav.jsp" %>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1>About the CodeU Crossing App</h1>
      <p>
        This travel application helps users across the world learn more about different countries as well as help plan their next vacation.
        Users have the ability to create destinations of countries that have not been visited by others yet, share images, start forums, rank their
        favorite spots as well as provide tips for the rest of the world!
      </p>

      <ul>
        <li><strong>Algorithms and data structures:</strong> A popular data structure commonly used throughout the production of our travel application
         was a hashmap. We noticed the inefficiency of the list structure used for most object stores, and were able to convert these lists to hashmaps,
        allowing our search procedures to run in constant time, rather than linear.</li>
        <li><strong>Look and feel:</strong> Through simple css styling, we were able to take the design and layout provided to us by
        CodeU, and slightly modify the text and formatting to make it something unique and special to us. Our main goal was to keep the site as sleek as possible,
        meanwhile carefully choosing colors that are easy on the eye. </li>
        <li><strong>Customization:</strong> Our team of three hails from very different hometowns, individually leaning us towards very unique hobbies. One source of recreation that we share (and many
         others as well) is traveling. We wanted to create a web application that allowed users like us to share our own experiences on our adventures! Hopefully our advice can
        help plan others' vacations in the near future.</li>
      </ul>

      <h1>About the Developers</h1>

      <div id="developers">
      <div>
      <strong><u>Vibha Agarwal</strong></u>
      <p>I'm a sophomore at MIT. My hobbies include reading, dancing,
      fire-spinning, traveling/adventuring, going on long car rides with friends,
      working on projects, cooking/eating, and sleeping.</p>
      </div>

      <div>
      <strong><u>Jenessa Cordero</strong></u>
      <p>Hi! My name is Jenessa and I will be going into my sophomore year at the University of California - Berkeley. I'm currently studying cognitive
      science and data science and hope to someday learn Korean!</p>
      </div>

      <div>
      <strong><u>Christopher Avalos</strong></u>
      <p>I'm a sophomore at Brown University and I plan on studying Computer
      Science and East Asian Studies (with a focus on Japanese). Some of my
      hobbies include reading, playing the piano, video games, and
      listening to music.</p>
      </div>
      </div>
    </div>
  </div>
</body>
</html>
