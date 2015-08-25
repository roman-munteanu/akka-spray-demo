package com.munteanu.demo.layout

import com.munteanu.demo.domain.Project

/**
 * Created by romunteanu on 8/11/2015.
 */
object IndexLayout {
  def apply(projects: Seq[Project]) =
    <html>
      <head>
        <title>Spray Slick Demo</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link href="/css/main.css" rel="stylesheet" />

        <script src="/src/jquery-1.11.3.min.js"></script>
        <script src="/src/main.js"></script>
      </head>
      <body>
        {header}

        <fieldset>
          <legend>Add project</legend>
          <form action="/rest/projects" method="post" id="project_form">
            <input name="id" value="" placeholder="id" />
            <input name="name" value="" placeholder="name" />
            <textarea name="description" placeholder="description"></textarea>
            <button type="submit">Submit</button>
          </form>
        </fieldset>

        <ul>
          {renderProjects(projects)}
        </ul>

        {footer}
      </body>
    </html>

  private def header =
    <header>
      <h1>Sub-Zero Demo</h1>
    </header>

  private def footer =
    <footer>
      <p>footer</p>
    </footer>

  private def renderProjects(projects: Seq[Project]) =
    for (project <- projects) yield
      <li>
        <b>{project.id.get.toString}</b> {project.name} <em>{project.description}</em> <button type="button" data-ident={project.id.get.toString} class="del-btn">remove</button>
      </li>
}
