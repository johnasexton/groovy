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
                    }
                }
        }
        stage('Docker Build') {
            steps {
                // docker steps
                echo 'docker version'
                echo "docker build -t johnasexton/${MODULE}:${TAG} -f ${MODULE}/Dockerfile"
                echo "cd ${MODULE} && ls -la && docker build -t johnasexton/${MODULE}:${TAG} ."
            }
        }
    }
}
