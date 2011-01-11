package com.bubblefoundry.gitignore

import sbt._
import processor.BasicProcessor

import java.io.{BufferedWriter, FileWriter}

class GitIgnoreProcessor extends BasicProcessor {
  def apply(project: Project, args: String) {
    // project.log.info("Processor test.  args: '" + args + "'")
    val gitignoreName = ".gitignore"
    val ignored = List("target/", "lib_managed/", "project/boot/", "project/build/target/", "project/plugins/target/", "project/plugins/lib_managed/", "project/plugins/src_managed/")
    
    val projectPath = project.rootProject.info.projectPath
    val gitignorePath = projectPath / gitignoreName
    val gitignoreFile = gitignorePath.asFile
    
    // first make sure the file exists
    if (!gitignorePath.exists) {
      // don't catch exceptions as we want to fail hard if we can't create the file
      gitignoreFile.createNewFile()
    }
    
    val alreadyIgnored = io.Source.fromFile(gitignoreFile).getLines.toList.map(_.trim) // getLines is supposed to strip newlines but it didn't seem to for me
    val toIgnore = ignored -- alreadyIgnored
    
    val out = new BufferedWriter(new java.io.FileWriter(gitignoreFile, true)) // make sure we're appending
    toIgnore.foreach(ignore => {
      out.write(ignore)
      out.newLine
    })
    out.flush
    out.close
  }
}