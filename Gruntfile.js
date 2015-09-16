module.exports = function(grunt) {

    var port           = 8080;
    var jarName        = 'server.jar';
    var dieUrl         = '/please_die/';
    var idejarLocation = 'backend/classes/artifacts/embrace_the_darkness_jar/embrace_the_darkness.jar';

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        shell: {
            options: {
                stdout: true,
                stderr: true,
            },
            runServer: {
                command: 'java -cp ' + jarName + ' main.Main ' + port
            }
        },

        http: {
            stop_jar: {
                options: {
                    url: 'http://127.0.0.1:' + port + '/' + dieUrl + '/',
                    callback: function(error, response, body) {
                        if(response.statusCode == 200) {
                            setTimeout(function() {
                                var fs = require('fs');
                                fs.unlink(jarName, function(err){});
                            }, 5000);
                        }
                    },
                    ignoreErrors: true,
                }
            }
        },

        watch: {
            fest: {
                files: ['frontend/fest/**/*.xml'],
                tasks: ['fest'],
                options: {
                    atBegin: false,
                }
            },
            server: {
                files: [
                    'frontend/static/**/*'
                ],
                tasks: ['copy:main'],
                options: {
                    interrupt: true,
                    livereload: true,
                }
            },
            jar: {
                files: [
                    idejarLocation
                ],
                tasks: ['http:stop_jar'],
                options: {
                    interrupt: true,
                    livereload: true,
                    event: ['added', 'changed'],
                }
            },
            run_jar: {
                files: [
                    jarName
                ],
                tasks: ['copy:jar', 'shell:runServer'],
                options: {
                    interrupt: true,
                    livereload: true,
                    event: ['deleted'],
                }
            },
        },

        fest: {
            templates: {
                files: [{
                    expand: true,
                    cwd: 'frontend/templates',
                    src: '**/*.xml',
                    dest: 'public_html/js/tmpl'
                }],
                options: {
                    template: function (data) { /* задаем формат функции-шаблона */
                        return grunt.template.process(
                            'var <%= name %>Tmpl = <%= contents %> ;', /* присваиваем функцию-шаблон переменной */
                            {data: data}
                        );
                    }
                }
            }
        },

        copy: {
            main: {
                expand: true,
                cwd: 'frontend/static/',
                src: '**',
                dest: 'public_html/',
                flatten: false,
            },
            jar: {
                expand: true,
                cwd: 'backend/classes/artifacts/embrace_the_darkness_jar/',
                src: 'embrace_the_darkness.jar',
                dest: '',
                rename: function(dest, src) {
                    return dest + jarName;
                }
            }
        },

        concurrent: {
            target: ['watch', 'shell:runServer'],
            options: {
                logConcurrentOutput: true
            },
        },

    });


    grunt.loadNpmTasks('grunt-contrib-requirejs');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-concurrent');
    grunt.loadNpmTasks('grunt-shell-spawn');
    grunt.loadNpmTasks('grunt-fest');
    grunt.loadNpmTasks('grunt-sass');
    grunt.loadNpmTasks('grunt-concat-css');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-http');

    grunt.registerTask('default', ['fest', 'copy']);
    grunt.registerTask('run', ['shell:runServer']);
    grunt.registerTask('dev_run', ['concurrent']);

}