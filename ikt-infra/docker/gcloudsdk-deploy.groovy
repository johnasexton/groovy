pipeline {
agent {
    node {
        label 'master'
    }
}
stages {
    stage('docker build & tag') {
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
                    echo "do stuff"
                    sh "ls -la"
                    // sh 'docker version'
                    echo "docker build -t johnasexton/${MODULE}:${TAG} -f ${MODULE}/Dockerfile"
                    // sh docker build -t "johnasexton/${MODULE}:${TAG}" .
                    echo "docker push johnasexton/${MODULE}:${TAG}"
                }
            }
        }
    }
}
