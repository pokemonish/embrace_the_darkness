module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        shell: {
            options: {
                stdout: true,
                stderr: true
            },
            buildServer: {
                command: (process.platform === 'win32' ? 'build_server.bat' : 'sh build_server.sh')
            },
            runServer: {
                command: 'java -cp server.jar main.Main 8080'
            }
        },

        watch: {
            fest: {
                files: ['frontend/fest/**/*.xml'],
                tasks: ['fest'],
                options: {
                    atBegin: true
                }
            },
            server: {
                files: [
                    'frontend/static/**/*'
                ],
                tasks: ['copy:main'],
                options: {
                    interrupt: true,
                    livereload: true
                }
            }
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
    grunt.loadNpmTasks('grunt-shell');
    grunt.loadNpmTasks('grunt-fest');
    grunt.loadNpmTasks('grunt-sass');
    grunt.loadNpmTasks('grunt-concat-css');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');


    grunt.registerTask('default', ['fest', 'copy:main']);
    grunt.registerTask('dev', ['fest', 'copy:main', 'concurrent']);
    grunt.registerTask('build', ['fest', 'copy:main', 'shell:buildServer']);
    grunt.registerTask('run', ['shell:runServer']);

}