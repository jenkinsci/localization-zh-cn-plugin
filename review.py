import argparse
import requests
import json
from jproperties import Properties

global GITHUB_TOKEN


# Get raw content from github.
def raw_contents(repo, branch, file):
    url = 'https://raw.githubusercontent.com/{repo}/{branch}/{file}'.format(repo=repo, branch=branch, file=file)
    resp = requests.get(url, timeout=10)
    return resp.ok, resp.text


# Get default branch in repository
def default_branch(path_with_namespace):
    url = 'https://api.github.com/repos/{path_with_namespace}'.format(path_with_namespace=path_with_namespace)
    headers = {
        'Content-Type': 'application/vnd.github.v3+json',
        'Authorization': 'token ' + GITHUB_TOKEN
    }
    resp = requests.get(url, headers)
    repository = json.loads(resp.text)
    return repository['default_branch']


# Is repository exist
def stat_repository(path_with_namespace):
    url = 'https://api.github.com/repos/{path_with_namespace}'.format(path_with_namespace=path_with_namespace)
    headers = {
        'Content-Type': 'application/vnd.github.v3+json',
        'Authorization': 'token ' + GITHUB_TOKEN
    }
    resp = requests.get(url, headers)
    return resp.ok


def en_us_file_name_in_jenkins_core(current_file_name):
    return current_file_name.replace('_zh_CN', '')


def en_us_file_name_in_jenkins_plugins(current_file_name, plugin_name):
    return current_file_name.replace('plugins/{0}/'.format(plugin_name), '').replace('_zh_CN', '')


# Metadata of the files, original and translated files etc..
def files_metadata(changed_files):
    metadata = []
    non_exist_repositories = []
    for file in changed_files:
        if file.startswith('core') or file.startswith('cli'):
            jenkins_core_repository = 'jenkinsci/jenkins'
            core = {
                'repo': jenkins_core_repository,
                'default_branch': default_branch(jenkins_core_repository),
                'en_us_file_url': 'https://github.com/{repository}/blob/{branch}/{file}'.format(repository=jenkins_core_repository, branch=default_branch(jenkins_core_repository), file=en_us_file_name_in_jenkins_core(file)),
                "en_us_file_name": en_us_file_name_in_jenkins_core(file),
                "zh_cn_file_name": file
            }
            metadata.append(core)
        elif file.startswith('plugins'):
            plugin_name = file.split('/')[1]
            jenkins_plugin_repository = 'jenkinsci/' + plugin_name
            if stat_repository(jenkins_plugin_repository):
                plugin = {
                    'repo': jenkins_plugin_repository,
                    'default_branch': default_branch(jenkins_plugin_repository),
                    'en_us_file_url': 'https://github.com/{repository}/blob/{branch}/{file}'.format(repository=jenkins_plugin_repository, branch=default_branch(jenkins_plugin_repository), file=en_us_file_name_in_jenkins_plugins(file, plugin_name)),
                    "en_us_file_name": en_us_file_name_in_jenkins_plugins(file, plugin_name),
                    "zh_cn_file_name": file
                }
                metadata.append(plugin)
            else:
                non_exist_repositories.append({
                    'repo': jenkins_plugin_repository,
                    'default_branch': None,
                    "en_us_file_name": en_us_file_name_in_jenkins_plugins(file, plugin_name),
                    'zh_cn_file_name': file
                })

    return metadata, non_exist_repositories


# Get the original content and translated content
def files_translation(files):
    translations = []
    non_exist_files = []
    for file in files:
        exists, en_us_content = raw_contents(file['repo'], file['default_branch'], file['en_us_file_name'])
        if not exists:
            non_exist_files.append({
                'repo': file['repo'],
                'default_branch': file['default_branch'],
                'en_us_file_name': file['en_us_file_name'],
                'zh_cn_file_name': file['zh_cn_file_name']
            })
        else:
            en_us_props = Properties()
            en_us_props.load(en_us_content, encoding='utf-8')
            with open(file['zh_cn_file_name'], "rb") as f:
                zh_cn_props = Properties()
                zh_cn_props.load(f, "utf-8")

            translation = {
                'en_us_file_url': file['en_us_file_url'],
                'zh_cn_file_name': file['zh_cn_file_name'],
                'en_us_content': en_us_props,
                'zh_cn_content': zh_cn_props,
            }
            translations.append(translation)
    return translations, non_exist_files


# Output result in markdown format
def output_markdown(translations, non_exist_repositories, non_exist_files):
    string = []
    for result in non_exist_repositories:
        string.append('#### 📘 ' + result['zh_cn_file_name'])
        string.append('⛔ 该本地化文件对应的原始代码仓库 `{0}` 不存在'.format(result['repo']))
    for result in non_exist_files:
        string.append('#### 📘 ' + result['zh_cn_file_name'])
        string.append('⛔ 代码仓库 {0} 的 {1} 分支中不存在原始文件 `{2}`'.format(result['repo'], result['default_branch'], result['en_us_file_name']))
    for result in translations:
        string.append('#### 📘 ' + result['zh_cn_file_name'])
        string.append("🇺🇸 [原始文件]({0})".format(result['en_us_file_url']))
        string.append("```")
        for k1, v1 in result['en_us_content'].items():
            string.append('{0}={1}'.format(k1, v1.data))
        string.append("```")
        string.append("🇨🇳 本地化文件")
        string.append("```")
        for k2, v2 in result['zh_cn_content'].items():
            string.append('{0}={1}'.format(k2, v2.data))
        string.append("```\n")

    return "\n".join(string)


def main(arguments):
    global GITHUB_TOKEN
    GITHUB_TOKEN = arguments.token
    # Merge added files and modified files
    changed_files = arguments.add.split(',') + arguments.mod.split(',')
    # Metadata of the files, original and translated files etc..
    files, non_exist_repositories = files_metadata(changed_files)
    # Result of original content and translation content
    translations, non_exist_files = files_translation(files)
    # Output markdown format of the result
    markdown = output_markdown(translations, non_exist_repositories, non_exist_files)
    print(markdown)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Grab original and translated files for code review')
    parser.add_argument('--add', required=True, help='Comma-split of the added files')
    parser.add_argument('--mod', required=True, help='Comma-split of the modified files')
    parser.add_argument('--token', required=False, default='', help='Github token to avoid rate limit error')
    args = parser.parse_args()
    main(args)
