pipeline {
  agent {
      node {
          label 'master'
      }
  }
    stages {
        stage('GitHub checkout') {
            steps {
                checkout poll: false,
                  scm: [
                        $class: 'GitSCM',
                        branches: [[name: '*/main']],
                        extensions: [],
                        userRemoteConfigs: [[credentialsId: 'jenkins',
                        // url: 'git@github.com:johnasexton/docker.git']]
                        url: 'https://github.com/johnasexton/docker.git']]
                        ]
                script {
                        // list files in ${workspace}
                        sh "ls -la ${workspace}"
                        sh "ls -la ${workspace}/${MODULE}"
                        // stash Dockerfile for next stage 
                        stash includes: "${MODULE}/*", name: 'dockerfile'
                    }
                }
        }
        stage('Docker Build in container') {
          agent { kubernetes 'johnasexton-jenkins-agent' }
                  steps {
                    // unstash Dockerfile from previous stage
                    unstash 'dockerfile'
                    // docker build tag and push to docker hub
                    sh "ls -la $workspace"
                    sh "ls -la $workspace/${MODULE}"
                    echo 'which docker'
                    echo "docker version"
                    echo "docker build -t johnasexton/${MODULE}:${TAG} -f $workspace/${MODULE}/Dockerfile"
                    echo "docker push johnasexton/${MODULE}:${TAG}"
            }
        }
    }
}
