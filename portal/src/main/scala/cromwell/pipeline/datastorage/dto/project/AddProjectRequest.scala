package cromwell.pipeline.datastorage.dto.project

import cromwell.pipeline.datastorage.dto.UserId

final case class AddProjectRequest(ownerId: UserId, name: String, repository: String)
