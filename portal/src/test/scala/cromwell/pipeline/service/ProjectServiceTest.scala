package cromwell.pipeline.service

import cromwell.pipeline.datastorage.dao.repository.ProjectRepository
import cromwell.pipeline.datastorage.dto.project.AddProjectRequest
import cromwell.pipeline.datastorage.dto.{ Project, ProjectId, UserId }
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, WordSpec }

import scala.concurrent.{ ExecutionContext, Future }

class ProjectServiceTest extends WordSpec with Matchers with MockFactory with ScalaFutures {

  implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  private val projectRepository = stub[ProjectRepository]
  private val projectService = new ProjectService(projectRepository)

  "ProjectServiceTest" when {

    "addProject" should {
      "return id of a new project" in {
        val request =
          AddProjectRequest(
            ownerId = UserId("userId"),
            name = "projectName",
            repository = "repositoryName"
          )
        val projectId = ProjectId("projectId")

        (projectRepository.addProject _ when *).returns(Future(projectId))

        whenReady(projectService.addProject(request)) { _ shouldBe projectId }
      }
    }

    "deactivateProjectById" should {
      "return deactivated project" in {

        val projectId = ProjectId("projectId")
        val project =
          Project(
            projectId = projectId,
            ownerId = UserId("userId"),
            name = "projectName",
            repository = "repositoryName",
            active = false
          )
        (projectRepository.deactivateProjectById _ when projectId).returns(Future(0))
        (projectRepository.getProjectById _ when *).returns(Future(Some(project)))

        whenReady(projectService.deactivateProjectById(projectId)) { _ shouldBe Some(project) }
      }
    }

    "getProjectById" should {
      "return project with corresponding id" in {
        val projectId = ProjectId("projectId")
        val project =
          Project(
            projectId = projectId,
            ownerId = UserId("userId"),
            name = "projectName",
            repository = "repositoryName",
            active = false
          )

        (projectRepository.getProjectById _ when projectId).returns(Future(Some(project)))

        whenReady(projectService.getProjectById(projectId)) { _ shouldBe Some(project) }
      }
    }
  }
}
